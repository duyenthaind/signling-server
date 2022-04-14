package org.thaind.signaling.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.common.Constants;
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
        if (!packetService.isPresent()) {
            LOGGER.error(String.format("Packet %s not found supported processor ", packet));
            Packet notFoundServicePacket = new Packet();
            notFoundServicePacket.setServiceType(Constants.PacketServiceType.NOT_FOUND);
            notFoundServicePacket.setBody(new JSONObject());
            userConnection.sendPacket(notFoundServicePacket);
        } else {
            packetService.get().processPacket(packet, userConnection);
        }
    }

    public static boolean validateEmptyRequest(Packet packet, UserConnection userConnection) {
        if (packet.getBody() == null) {
            Packet resPacket = new Packet();
            resPacket.setServiceType(packet.getServiceType());
            resPacket.setField(Constants.ResponseField.RES.getField(), 100);
            resPacket.setField(Constants.ResponseField.MESSAGE.getField(), "body_must_not_be_null");
            userConnection.sendPacket(resPacket);
            return false;
        }
        return true;
    }

    protected abstract void process(Packet packet, UserConnection userConnection);

}
