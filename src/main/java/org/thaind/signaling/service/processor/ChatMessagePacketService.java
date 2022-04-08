package org.thaind.signaling.service.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.cache.ChatConversationManager;
import org.thaind.signaling.common.MagicHolder;
import org.thaind.signaling.dto.ChatConversation;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.hibernate.entity.ConversationEntity;
import org.thaind.signaling.hibernate.entity.ConversationUserEntity;
import org.thaind.signaling.hibernate.entity.MessageEntity;
import org.thaind.signaling.processors.Processor;
import org.thaind.signaling.repository.ConversationRepository;
import org.thaind.signaling.repository.ConversationUserRepository;
import org.thaind.signaling.repository.MessageRepository;
import org.thaind.signaling.repository.impl.ConversationRepositoryImpl;
import org.thaind.signaling.repository.impl.ConversationUserRepositoryImpl;
import org.thaind.signaling.repository.impl.MessageRepositoryImpl;
import org.thaind.signaling.service.PacketService;

import java.util.Optional;
import java.util.UUID;

import static org.thaind.signaling.common.Constants.ResponseField;

/**
 * @author duyenthai
 */

/**
 * {
 * "convId":"convId",
 * "body":"{message}"
 * }
 */
public class ChatMessagePacketService implements PacketService {

    private static final Logger LOGGER = LogManager.getLogger("ChatMessagePacketProcessor");

    private final ConversationRepository conversationRepository = new ConversationRepositoryImpl();
    private final MessageRepository messageRepository = new MessageRepositoryImpl();
    private final ConversationUserRepository conversationUserRepository = new ConversationUserRepositoryImpl();

    @Override
    public void processPacket(Packet packet, UserConnection userConnection) {
        Packet resPacket = new Packet();
        resPacket.setServiceType(packet.getServiceType());
        JSONObject requestBody = packet.getBody();
        if (!Processor.validateEmptyRequest(packet, userConnection)) {
            return;
        }
        int magic = 0;
        String msg = "success";
        MagicHolder magicHolder = new MagicHolder(magic, msg);
        if (userConnection.isForCall()) {
            magicHolder.setMagic(-1);
            magicHolder.setMsg("Not for call");
        }
        ChatConversation chatConversation = null;
        String conversationId = "";
        String body = "";
        if (magicHolder.getMagic() == 0) {
            conversationId = requestBody.optString("convId");
            chatConversation = validateAndGetConversation(conversationId, magicHolder);
        }
        if (magicHolder.getMagic() == 0 && chatConversation != null) {
            body = requestBody.optString("body");
            saveMessageAndSendToOtherConnection(body, userConnection, chatConversation, magicHolder);
        }
        if (magicHolder.getMagic() == 0 && chatConversation != null) {
            Packet toPacket = new Packet();
            toPacket.setServiceType(packet.getServiceType());
            toPacket.setField(ResponseField.FROM_USER.getField(), userConnection.getUserId());
            toPacket.setField(ResponseField.DATA.getField(), packet.getBody());
            chatConversation.sendPacketToOtherConnection(toPacket, userConnection);
        }

        LOGGER.debug(String.format("Chat send message from user %s to conv %s, res %s", userConnection.getUserId(), conversationId, resPacket.getBodyString()));
        resPacket.setField(ResponseField.RES.getField(), magicHolder.getMagic());
        resPacket.setField(ResponseField.MESSAGE.getField(), magicHolder.getMsg());
        userConnection.sendPacket(resPacket);
    }

    ChatConversation validateAndGetConversation(String conversationId, MagicHolder magicHolder) {
        if (StringUtils.isEmpty(conversationId)) {
            magicHolder.setMagic(1);
            magicHolder.setMsg("Not found conversationId");
            return null;
        }
        ChatConversation chatConversation = ChatConversationManager.getInstance().getConversation(conversationId);
        if (chatConversation == null) {
            magicHolder.setMagic(2);
            magicHolder.setMsg("Conversation has not been created, need create before joining");
            return null;
        }
        return chatConversation;
    }

    void saveMessageAndSendToOtherConnection(String body, UserConnection userConnection, ChatConversation chatConversation, MagicHolder magicHolder) {
        chatConversation.setLastTimeNewMessage(System.currentTimeMillis() / 1000L);
        long seq = chatConversation.incrementSeqAndGet();
        ConversationEntity conversationEntity = chatConversation.getConversationEntity();
        conversationEntity.setSeq(seq);
        conversationRepository.update(conversationEntity);
        MessageEntity message = saveNewMessage(body, userConnection.getUserId(), conversationEntity);
        if (message == null) {
            magicHolder.setMagic(3);
            magicHolder.setMsg("Cannot save message");
            return;
        }
        createOrUpdateConversationUser(userConnection.getUserId(), message, conversationEntity.getId());
    }

    MessageEntity saveNewMessage(String body, String userId, ConversationEntity conversationEntity) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(UUID.randomUUID().toString());
        messageEntity.setContent(conversationEntity.getId());
        messageEntity.setSeq(conversationEntity.getSeq());
        messageEntity.setContent(body);
        messageEntity.setFromUser(userId);
        return messageRepository.save(messageEntity);
    }

    void createOrUpdateConversationUser(String userId, MessageEntity message, String conversationId) {
        Optional<ConversationUserEntity> conversationUser = conversationUserRepository.findByUserIdAndConversationId(userId, conversationId);
        if (conversationUser.isPresent()) {
            ConversationUserEntity conversationUserEntity = conversationUser.get();
            conversationUserEntity.setLastSeq(message.getSeq());
            conversationUserEntity.setLastSeqSeen(message.getSeq());
            conversationUserRepository.update(conversationUserEntity);
        } else {
            ConversationUserEntity conversationUserEntity = new ConversationUserEntity();
            conversationUserEntity.setId(UUID.randomUUID().toString());
            conversationUserEntity.setUserId(userId);
            conversationUserEntity.setConvId(conversationId);
            conversationUserEntity.setLastSeq(message.getSeq());
            conversationUserEntity.setLastSeqSeen(message.getSeq());
            conversationUserRepository.save(conversationUserEntity);
        }
    }

}
