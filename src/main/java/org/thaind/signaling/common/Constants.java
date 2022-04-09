package org.thaind.signaling.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author duyenthai
 */
public class Constants {
    public enum PacketServiceType {
        NOT_FOUND(404, "unsupported"),
        SAMPLE(-100, "sample"),
        PING(99, "ping"),
        PONG(100, "pong"),
        AUTHENTICATE(1, "authenticate"),
        JOIN_CONVERSATION(2, "user_join_conversation"),
        JOIN_ROOM(3, "user_join_room"),
        USER_JOINED_ROOM(4, "user_joined_room"),
        USER_LEAVE_ROOM_OR_TIMEOUT(5, "user_leaved_room"),
        TIMEOUT_PICKUP(6, "timeout_pickup_call"),
        CHAT_CREATE_CONVERSATION(7, "chat_create_conversation"),
        CALL_CREATE_ROOM(8, "call_create_room"),
        CHAT_MESSAGE(9, "chat_message"),
        CALL_SDP(10, "call_sdp"),
        CALL_ICE_CANDIDATE(11, "call_ice_candidate"),
        CHAT_GET_MESSAGE(12,"chat_get_message"),
        ;
        private final int serviceType;
        private final String name;

        PacketServiceType(int serviceType, String name) {
            this.serviceType = serviceType;
            this.name = name;
        }

        public static PacketServiceType fromServices(int service) {
            for (PacketServiceType index : values()) {
                if (index.serviceType == service) {
                    return index;
                }
            }
            return null;
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
        NOT_FOUND(404),
        USER_NOT_FOUND(405),
        ;
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

    public enum SocketIoEvent {
        EVENT_PACKET("EventPacket"),
        ;
        private final String event;

        SocketIoEvent(String event) {
            this.event = event;
        }

        public String getEvent() {
            return event;
        }
    }

    public enum ResponseField {
        RES("r"),
        MESSAGE("msg"),
        STATUS("status"),
        ACCESS_TOKEN("access_token"),
        CONVERSATION_ID("conversation_id"),
        ROOM_ID("room_id"),
        DATA("data"),
        FROM_USER("from_user")
        ;
        private final String field;

        ResponseField(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }
}
