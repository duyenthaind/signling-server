package org.thaind.signaling.service.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thaind.signaling.service.PacketService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.thaind.signaling.common.Constants.PacketServiceType;

/**
 * @author duyenthai
 */
public class PacketProcessorFactory {

    private static final Logger LOGGER = LogManager.getLogger("PacketProcessorFactory");
    private static final Map<PacketServiceType, Class<? extends PacketService>> SERVICES = new ConcurrentHashMap<>();

    private PacketProcessorFactory() {
    }

    static {
        SERVICES.put(PacketServiceType.SAMPLE, SamplePacketService.class);
        SERVICES.put(PacketServiceType.PING, PingPacketService.class);
        SERVICES.put(PacketServiceType.AUTHENTICATE, AuthenticationPacketService.class);
        SERVICES.put(PacketServiceType.JOIN_CONVERSATION, JoinConversationPacketService.class);
        SERVICES.put(PacketServiceType.JOIN_ROOM, JoinRoomPacketService.class);
        SERVICES.put(PacketServiceType.CHAT_CREATE_CONVERSATION, ChatCreateConversationPacketService.class);
        SERVICES.put(PacketServiceType.CALL_CREATE_ROOM, CallCreateRoomPacketService.class);
        SERVICES.put(PacketServiceType.CHAT_MESSAGE, ChatMessagePacketService.class);
        SERVICES.put(PacketServiceType.CALL_SDP, CallSdpCandidatePacketService.class);
        SERVICES.put(PacketServiceType.CALL_ICE_CANDIDATE, CallSdpCandidatePacketService.class);
    }

    public static Optional<PacketService> factoryService(PacketServiceType packetServiceType) {
        try {
            Class<? extends PacketService> clazz = SERVICES.get(packetServiceType);
            if (clazz == null) {
                return Optional.empty();
            }
            return Optional.of(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception ex) {
            LOGGER.error("Error when trying create service ", ex);
        }
        return Optional.empty();
    }
}
