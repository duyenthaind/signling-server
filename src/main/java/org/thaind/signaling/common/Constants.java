package org.thaind.signaling.common;

/**
 * @author duyenthai
 */
public class Constants {
    public enum PacketServiceType {
        SAMPLE(-100, "sample"),
        ;
        private final int serviceType;
        private final String name;

        PacketServiceType(int serviceType, String name) {
            this.serviceType = serviceType;
            this.name = name;
        }

        public int getServiceType() {
            return serviceType;
        }

        public String getName() {
            return name;
        }
    }
}
