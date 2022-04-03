package org.thaind.signaling.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.service.PacketService;
import org.thaind.signaling.service.processor.PacketProcessorFactory;

import java.util.Optional;

/**
 * @author duyenthai
 */
public abstract class Processor {

    private static final Logger LOGGER = LogManager.getLogger("Processor");

    public static void processPacket(Packet packet, UserConnection userConnection) {
        Optional<PacketService> packetService = PacketProcessorFactory.factoryService(packet.getServiceType());
        if (packetService.isEmpty()) {
            LOGGER.error(String.format("Packet %s not found supported processor ", packet));
            Packet notFoundServicePacket = new Packet(packet.getServiceType());
            // todo add body
            userConnection.sendPacket(notFoundServicePacket);
        } else {
            packetService.get().processPacket(packet, userConnection);
        }
    }

    protected abstract void process(Packet packet, UserConnection userConnection);

}
