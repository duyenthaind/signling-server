package org.thaind.signaling.dto;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.thaind.signaling.common.Constants;

/**
 * @author duyenthai
 */
public class Packet {
    private Constants.PacketServiceType serviceType;
    private JSONObject body;

    public Packet() {
    }

    public Packet(Constants.PacketServiceType serviceType, String body) {
        this.serviceType = serviceType;
        if (!StringUtils.isEmpty(body)) {
            this.body = new JSONObject(body);
        }
    }

    public Packet(Constants.PacketServiceType serviceType, JSONObject body) {
        this.serviceType = serviceType;
        this.body = body;
    }

    public Constants.PacketServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(Constants.PacketServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public void setField(String key, Object value) {
        if (body == null) {
            body = new JSONObject();
        }
        body.put(key, value);
    }

    public String getBodyString() {
        if (body == null) {
            return "";
        }
        return body.toString();
    }

    @Override
    public String toString() {
        return "Packet{" +
                "serviceType=" + serviceType +
                ", body='" + body + '\'' +
                '}';
    }
}
