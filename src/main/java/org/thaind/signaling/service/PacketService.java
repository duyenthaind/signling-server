package org.thaind.signaling.service;

import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;

/**
 * @author duyenthai
 */
public interface PacketService {
    void processPacket(Packet packet, UserConnection userConnection);
}
