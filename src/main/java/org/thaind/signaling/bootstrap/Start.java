package org.thaind.signaling.bootstrap;

import org.thaind.signaling.common.Config;
import org.thaind.signaling.websocket.WebSocketServer;
import org.thaind.signaling.worker.PingWorker;

/**
 * @author duyenthai
 */
/*
This is bootstrap class of this project
 */
    //todo add config timeout ping, port of server
public class Start {
    public static void main(String[] args) throws Exception {
        Config.loadConfig();
        WebSocketServer webSocketServer = new WebSocketServer();
        webSocketServer.start(8888);
        Thread pingWorker = new Thread(new PingWorker());
        pingWorker.start();
    }
}
