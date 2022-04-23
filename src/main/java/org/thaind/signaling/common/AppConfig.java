package org.thaind.signaling.common;

/**
 * @author duyenthaind
 */
public class AppConfig {
    private int websocketServerPort = 8088;
    private int socketIoServerPort = 8808;
    private String jwtSecret = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY0OTEyMjc2MiwiaWF0IjoxNjQ5MTIyNzYyfQ.nEsZzxLDXOYJQzbpyXemkqF2wONJSufoKkbl1BlKK0A";
    private int timeoutPing = 30;
    private String certFilePath = "MyDSKeyStore.jks";
    private String certPassword = "123456";

    public int getWebsocketServerPort() {
        return websocketServerPort;
    }

    public void setWebsocketServerPort(int websocketServerPort) {
        this.websocketServerPort = websocketServerPort;
    }

    public int getSocketIoServerPort() {
        return socketIoServerPort;
    }

    public void setSocketIoServerPort(int socketIoServerPort) {
        this.socketIoServerPort = socketIoServerPort;
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

    public String getCertFilePath() {
        return certFilePath;
    }

    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "websocketServerPort=" + websocketServerPort +
                ", socketIoServerPort=" + socketIoServerPort +
                ", jwtSecret='" + jwtSecret + '\'' +
                ", timeoutPing=" + timeoutPing +
                ", certFilePath='" + certFilePath + '\'' +
                ", certPassword='" + certPassword + '\'' +
                '}';
    }
}
