package org.thaind.signaling.service.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.cache.ChatConversationManager;
import org.thaind.signaling.dto.ChatConversation;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.hibernate.entity.ConversationEntity;
import org.thaind.signaling.hibernate.entity.MessageEntity;
import org.thaind.signaling.processors.Processor;
import org.thaind.signaling.repository.MessageRepository;
import org.thaind.signaling.repository.impl.MessageRepositoryImpl;
import org.thaind.signaling.service.PacketService;

import java.util.List;

import static org.thaind.signaling.common.Constants.ResponseField;

/**
 * @author duyenthai
 */

/**
 * {
 * "convId":"convId",
 * "fromSeq":fromSeq,
 * amount: amount(<10)
 * }
 */
public class ChatGetMessagePacketService implements PacketService {

    private static final Logger LOGGER = LogManager.getLogger("ChatGetMessageProcessor");

    private final MessageRepository repository = new MessageRepositoryImpl();

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
            msg = "Not for chat";
        }
        String conversationId = requestBody.optString("convId", "");
        int fromSeq = requestBody.optInt("fromSeq");
        int amount = requestBody.optInt("amount");
        if (magic == 0 && StringUtils.isEmpty(conversationId)) {
            LOGGER.error("ConversationId not found in payload, this request is malformed");
            magic = 1;
            msg = "ConversationId is invalid";
        }
        if (magic == 0 && (amount > 10 || amount < 0)) {
            magic = 2;
            msg = "Amount is in range 0 to 10";
        }
        if (magic == 0) {
            ChatConversation chatConversation = ChatConversationManager.getInstance().getConversation(conversationId);
            if (chatConversation == null) {
                magic = 3;
                msg = "Create conversation first";
            } else {
                String userId = userConnection.getUserId();
                ConversationEntity conversationEntity = chatConversation.getConversationEntity();
                if (!conversationEntity.getCreator().equals(userId) && !conversationEntity.getWithUser().equals(userId)) {
                    magic = 4;
                    msg = "Not in conversation";
                }
                if(magic == 0){
                    List<MessageEntity> list = repository.getMessageFromConversation(conversationId, fromSeq, amount);
                    resPacket.setField(ResponseField.DATA.getField(), list);
                }
            }
        }
        resPacket.setField(ResponseField.RES.getField(), magic);
        resPacket.setField(ResponseField.MESSAGE.getField(), msg);
        userConnection.sendPacket(resPacket);
    }
}
