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
    private static final Map<Integer, Class<? extends PacketService>> SERVICES = new ConcurrentHashMap<>();

    private PacketProcessorFactory() {
    }

    static {
        SERVICES.put(PacketServiceType.SAMPLE.getServiceType(), SamplePacketService.class);
    }

    public static Optional<PacketService> factoryService(int serviceType) {
        try {
            Class<? extends PacketService> clazz = SERVICES.get(serviceType);
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
