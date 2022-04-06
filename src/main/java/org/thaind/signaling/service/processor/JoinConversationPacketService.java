package org.thaind.signaling.service.processor;

/**
 * @author duyenthai
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.cache.ChatConversationManager;
import org.thaind.signaling.cache.UserConnectionManager;
import org.thaind.signaling.common.Constants;
import org.thaind.signaling.dto.ChatConversation;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.dto.internal.protocol.Response;
import org.thaind.signaling.hibernate.entity.ConversationEntity;
import org.thaind.signaling.processors.Processor;
import org.thaind.signaling.service.PacketService;

import static org.thaind.signaling.common.Constants.ResponseField;

/**
 * {"convId":"convId"}
 */
public class JoinConversationPacketService implements PacketService {

    private static final Logger LOGGER = LogManager.getLogger("JoinConversationPacketProcessor");

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
        if (magic == 0 && StringUtils.isEmpty(conversationId)) {
            LOGGER.error("ConversationId not found in payload, this request is malformed");
            magic = 1;
            msg = "ConversationId is invalid";
        }
        if (magic == 0) {
            ChatConversation chatConversation = getOrCreateNewConversation(conversationId);
            LOGGER.info(String.format("Get conversation with convId %s, last time new message %s", chatConversation.getLastTimeNewMessage()));
            Response internalResponse = chatConversation.addConnection(userConnection);
            if (Response.ok().equals(internalResponse)) {
                Response resAddOther = addOtherConnection(userConnection.getUserId(), chatConversation);
                if (!Response.ok().equals(resAddOther)) {
                    magic = resAddOther.getRes();
                    msg = resAddOther.getMessage();
                }
            } else {
                magic = internalResponse.getRes();
                msg = internalResponse.getMessage();
            }
        }
        LOGGER.info(String.format("User %s join conversation res : %s, message %s, convId %s", userConnection.getUserId(), magic, msg, conversationId));
        resPacket.setField(ResponseField.RES.getField(), magic);
        resPacket.setField(ResponseField.MESSAGE.getField(), msg);
        userConnection.sendPacket(resPacket);

    }

    ChatConversation getOrCreateNewConversation(String conversationId) {
        ChatConversation chatConversation = ChatConversationManager.getInstance().getConversation(conversationId);
        if (chatConversation == null) {
            chatConversation = new ChatConversation(conversationId);
        }
        return chatConversation;
    }

    Response addOtherConnection(String currentUserId, ChatConversation chatConversation) {
        ConversationEntity conversationEntity = chatConversation.getConversationEntity();
        if (StringUtils.isEmpty(conversationEntity.getCreator()) || StringUtils.isEmpty(conversationEntity.getWithUser())) {
            LOGGER.error("From conversation entity, user of conversation not found");
            return new Response(Constants.ResponseType.USER_NOT_FOUND.getRes(), conversationEntity.getId());
        }
        String otherUserId = conversationEntity.getCreator().equals(currentUserId) ? conversationEntity.getWithUser() : conversationEntity.getCreator();
        UserConnection otherConnection = UserConnectionManager.getInstance().getConnectionOfUser(otherUserId);
        if (otherConnection != null) {
            return chatConversation.addConnection(otherConnection);
        }
        return Response.ok();
    }

}
