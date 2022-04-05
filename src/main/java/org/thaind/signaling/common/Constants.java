package org.thaind.signaling.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        NOT_PERMITTED(401),
        UNAUTHORIZED(403),
        NOT_FOUND(404);
        private final int res;

        ResponseType(int res) {
            this.res = res;
        }

        public int getRes() {
            return res;
        }
    }

    public enum JWTResponseAuthen {
        ALG_UNSUPPORTED(1, "alg_fault"),
        EXPIRED(2, "token_expired"),
        UNDEFINED(-1, "undefined");
        private final int res;
        private final String msg;

        JWTResponseAuthen(int res, String msg) {
            this.res = res;
            this.msg = msg;
        }

        public int getRes() {
            return res;
        }

        public String getMsg() {
            return msg;
        }

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "{ \"res\":" + res + ", \"msg\":\"" + msg + "\"}";
        }
    }
}
