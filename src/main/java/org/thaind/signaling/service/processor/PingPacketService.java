package org.thaind.signaling.service.processor;

import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.service.PacketService;

import static org.thaind.signaling.common.Constants.ResponseField;

/**
 * @author duyenthai
 */

/**
 * {}
 */
public class PingPacketService implements PacketService {
    @Override
    public void processPacket(Packet packet, UserConnection userConnection) {
        Packet res = new Packet();
        res.setServiceType(packet.getServiceType());
        res.setField(ResponseField.RES.getField(), 0);
        res.setField(ResponseField.MESSAGE.getField(), "Pong");

        userConnection.sendPacket(packet);
    }
}
