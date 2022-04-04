package org.thaind.signaling.common;

/**
 * @author duyenthai
 */
public class Constants {
    public enum PacketServiceType {
        SAMPLE(-100, "sample"),
        PING(99, "ping"),
        PONG(100, "pong"),
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

    public enum ResponseType {
        OK(0),
        NOT_PERMITTED(403),
        NOT_FOUND(404);
        private final int res;

        ResponseType(int res) {
            this.res = res;
        }

        public int getRes() {
            return res;
        }
    }
}
