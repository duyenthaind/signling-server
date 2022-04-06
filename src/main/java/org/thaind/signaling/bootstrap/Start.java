package org.thaind.signaling.bootstrap;

import org.thaind.signaling.common.AppConfig;
import org.thaind.signaling.common.Config;
import org.thaind.signaling.socketio.SocketIOListener;
import org.thaind.signaling.websocket.WebSocketServer;
import org.thaind.signaling.worker.PingWorker;

/**
 * @author duyenthai
 */
/**
This is bootstrap class of this project
 */
public class Start {
    public static void main(String[] args) throws Exception {
        Config.loadConfig();
        AppConfig appConfig = SingletonHolder.getBeanOrDefault(AppConfig.class);
        WebSocketServer webSocketServer = new WebSocketServer();
        webSocketServer.start(appConfig.getWebsocketServerPort());
        SocketIOListener.getInstance().start(appConfig.getSocketIoServerPort());
        Thread pingWorker = new Thread(new PingWorker());
        pingWorker.start();
    }
}
