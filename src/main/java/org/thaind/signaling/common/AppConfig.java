package org.thaind.signaling.common;

/**
 * @author duyenthaind
 */
public class AppConfig {
    private int serverPort = 8088;
    private String jwtSecret = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY0OTEyMjc2MiwiaWF0IjoxNjQ5MTIyNzYyfQ.nEsZzxLDXOYJQzbpyXemkqF2wONJSufoKkbl1BlKK0A";
    private int timeoutPing = 30;

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public int getTimeoutPing() {
        return timeoutPing;
    }

    public void setTimeoutPing(int timeoutPing) {
        this.timeoutPing = timeoutPing;
    }
}
