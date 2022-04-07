package org.thaind.signaling.service.processor;

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
        if(userConnection.isForCall()){
            magic = -1;
            msg = "Not for chat";
        }
        String fromUser = requestBody.optString("from", "");
        String toUser = requestBody.optString("to", "");

    }
}
