package org.thaind.signaling.dto;

/**
 * @author duyenthai
 */
public class EventPacket {
    private int service;
    private String body;

    public EventPacket() {
    }

    public EventPacket(int service, String body) {
        this.service = service;
        this.body = body;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
