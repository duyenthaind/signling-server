package org.thaind.signaling.dto;

/**
 * @author duyenthai
 */
public class Packet {
    private int serviceType;
    private String body;

    public Packet(int serviceType) {
        this.serviceType = serviceType;
    }

    public Packet() {
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "serviceType=" + serviceType +
                ", body='" + body + '\'' +
                '}';
    }
}
