package org.thaind.signaling.worker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.cache.UserConnectionManager;
import org.thaind.signaling.common.Constants;
import org.thaind.signaling.dto.Packet;
import org.thaind.signaling.dto.UserConnection;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author duyenthai
 */
public class PingWorker implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger("PingWorker");

    private static final long TIMEOUT_PING = 30 * 1000L;

    @Override
    public void run() {
        LOGGER.info("Start ping worker !");
        while (true) {
            LOGGER.info("Check ping and remove idle connection!");
            Packet pingPacket = new Packet();
            pingPacket.setServiceType(Constants.PacketServiceType.PING);
            pingPacket.setBody(new JSONObject());
            List<UserConnection> allConnections = UserConnectionManager.getInstance().getAllConnections();
            for (UserConnection connection : allConnections) {
                connection.sendPacket(pingPacket);
                if (System.currentTimeMillis() - connection.getLastTimeReceivePacket() > 2 * TIMEOUT_PING) {
                    connection.disconnect();
                }
            }
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (Exception ex) {
                LOGGER.error("Error  when trying to sleep ", ex);
            }
        }
    }

}
