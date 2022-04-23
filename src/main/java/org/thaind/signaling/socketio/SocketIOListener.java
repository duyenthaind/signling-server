package org.thaind.signaling.socketio;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thaind.signaling.common.Constants;
import org.thaind.signaling.dto.EventPacket;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;
import org.thaind.signaling.processors.Processor;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author duyenthai
 */
public class SocketIOListener {

    private static final Logger LOGGER = LogManager.getLogger("SocketIOListener");

    private final ObjectMapper objectMapper = new ObjectMapper();

    private SocketIOListener() {
    }

    public void start(int port, String certFilePath, String certPassword) {
        try {
            Configuration config = new Configuration();
            config.setHostname("0.0.0.0");
            config.setPort(port);
            InputStream certFilePathStream = new FileInputStream(certFilePath);
            config.setKeyStore(certFilePathStream);
            config.setKeyStorePassword(certPassword);

            SocketConfig socketConfig = new SocketConfig();
            socketConfig.setReuseAddress(true);
            config.setSocketConfig(socketConfig);

            final SocketIOServer server = new SocketIOServer(config);

            server.addConnectListener(client -> {
                UserConnection connection = new UserConnection();
                connection.setSocketIOClient(client);
                client.set("connection", connection);

//                client.sendEvent("Connect", "Connected");

                LOGGER.debug("onConnect, connection: " + connection);
            });

            server.addDisconnectListener(client -> {
                UserConnection connection = client.get("connection");
                if (connection == null) {
                    return;
                }

                LOGGER.debug("onDisconnect, connection: " + connection);

                connection.disconnect();
            });

            server.addEventListener("EventPacket", String.class, new DataListener<String>() {
                @Override
                public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                    LOGGER.info("onData, client: " + client.getSessionId() + ", data: " + data);
                    try{
                        EventPacket eventPacket = objectMapper.readValue(data, EventPacket.class);

                        Packet packet = new Packet(Constants.PacketServiceType.fromServices(eventPacket.getService()), eventPacket.getBody());
                        UserConnection connection = client.get("connection");
                        connection.setLastTimeReceivePacket(System.currentTimeMillis());

                        if (!StringUtils.isEmpty(connection.getUserId())
                                || eventPacket.getService() == Constants.PacketServiceType.AUTHENTICATE.getServiceType()
                                || eventPacket.getService() == Constants.PacketServiceType.PING.getServiceType()) {
                            Processor.processPacket(packet, connection);
                        } else {
                            LOGGER.info("Not logged in, close connection, packet: " + packet + "; connection : " + connection);
                        }
                    } catch (Exception ex){
                        LOGGER.error("Error listen event ", ex);
                    }

                }
            });

/*            server.addEventListener("EventPacket", EventPacket.class, (client, data, ackRequest) -> {
                try{
                    LOGGER.info("onData, client: " + client.getSessionId() + ", data: " + data);
                    Packet packet = new Packet(Constants.PacketServiceType.fromServices(data.getService()), data.getBody());
                    UserConnection connection = client.get("connection");
                    connection.setLastTimeReceivePacket(System.currentTimeMillis());

                    if (!StringUtils.isEmpty(connection.getUserId())
                            || data.getService() == Constants.PacketServiceType.AUTHENTICATE.getServiceType()
                            || data.getService() == Constants.PacketServiceType.PING.getServiceType()) {
                        Processor.processPacket(packet, connection);
                    } else {
                        LOGGER.info("Not logged in, close connection, packet: " + packet + "; connection : " + connection);
                    }
                } catch (Exception ex){
                    LOGGER.error("Error ", ex);
                }

            });*/

            server.start();

            LOGGER.info("WebSocket listening on " + port + " (SSL/TLS enabled)");
        } catch (Exception ex) {
            LOGGER.error("Exception  ", ex);
        }
    }

    public static SocketIOListener getInstance() {
        return Singleton.instance;
    }

    private static class Singleton {
        public static final SocketIOListener instance = new SocketIOListener();
    }
}
