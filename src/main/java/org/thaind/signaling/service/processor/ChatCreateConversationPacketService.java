package org.thaind.signaling.service.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.hibernate.entity.ConversationEntity;
import org.thaind.signaling.processors.Processor;
import org.thaind.signaling.repository.ConversationRepository;
import org.thaind.signaling.repository.impl.ConversationRepositoryImpl;
import org.thaind.signaling.service.PacketService;

import java.util.Optional;
import java.util.UUID;

import static org.thaind.signaling.common.Constants.ResponseField;

/**
 * @author duyenthaind
 */

/**
 * {
 * "from":"from",
 * "to":"to"
 * }
 */
public class ChatCreateConversationPacketService implements PacketService {

    private static final Logger LOGGER = LogManager.getLogger("ChatCreateConversationPacketProcessor");

    private final ConversationRepository repository = new ConversationRepositoryImpl();

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
        if (userConnection.isForCall()) {
            magic = -1;
            msg = "Not for call";
        }
        if (magic == 0) {
            String fromUser = requestBody.optString("from", "");
            String toUser = requestBody.optString("to", "");
            Optional<ConversationEntity> entity = repository.findByCreatorAndWithUser(fromUser, toUser);
            Optional<ConversationEntity> entityWith = repository.findByCreatorAndWithUser(toUser, fromUser);
            if (entity.isPresent() || entityWith.isPresent()) {
                String conversationId = entity.isPresent() ? entity.get().getId() : entityWith.get().getId();
                resPacket.setField(ResponseField.CONVERSATION_ID.getField(), conversationId);
            } else {
                ConversationEntity conversation = createNewConversation(fromUser, toUser);
                if (conversation != null) {
                    resPacket.setField(ResponseField.CONVERSATION_ID.getField(), conversation.getId());
                } else {
                    msg = "Failed to create conversation";
                    magic = 1;
                }
            }
        }
        LOGGER.info(String.format("Create conversation res: %s", resPacket.getBodyString()));
        resPacket.setField(ResponseField.RES.getField(), magic);
        resPacket.setField(ResponseField.MESSAGE.getField(), msg);
        userConnection.sendPacket(resPacket);
    }

    ConversationEntity createNewConversation(String fromUser, String toUser) {
        LOGGER.info(String.format("Create new db conversation fromUser %s withUser %s", fromUser, toUser));
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.setId(UUID.randomUUID().toString());
        conversationEntity.setCreator(fromUser);
        conversationEntity.setWithUser(toUser);
        conversationEntity.setSeq(0L);
        conversationEntity.setCreatedAt(System.currentTimeMillis());
        conversationEntity.setUpdatedAt(System.currentTimeMillis());
        return repository.save(conversationEntity);
    }
}
