package org.thaind.signaling.service.processor;

import org.json.JSONObject;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.processors.Processor;
import org.thaind.signaling.service.PacketService;

import static org.thaind.signaling.common.Constants.ResponseField;

/**
 * @author duyenthai
 */

/**
 * {
 * "roomId":"roomId",
 * //sic: sdp, ice candidate
 * "sic":{<sdp/candidate>}
 * }
 */
public class CallSdpCandidatePacketService implements PacketService {
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
        if (!userConnection.isForCall()) {
            magic = -1;
            msg = "Not for call";
        }
        if (userConnection.getCallRoom() == null) {
            magic = 1;
            msg = "Join room first";
        }
        String roomId = requestBody.optString("roomId");
        if (magic == 0) {
            if (userConnection.getCallRoom().getRoomId().equals(roomId)) {
                magic = 2;
                msg = "Wrong room";
            }
        }
        if (magic == 0) {
            userConnection.getCallRoom().sendPacketToOtherConnection(packet, userConnection);
        }
        resPacket.setField(ResponseField.RES.getField(), magic);
        resPacket.setField(ResponseField.MESSAGE.getField(), msg);
        userConnection.sendPacket(resPacket);
    }
}
