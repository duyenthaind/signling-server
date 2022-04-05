package org.thaind.signaling.common;

/**
 * @author duyenthaind
 */
public class AppConfig {
    private int serverPort;
    private String jwtSecret;
    private int timeoutPing;

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
