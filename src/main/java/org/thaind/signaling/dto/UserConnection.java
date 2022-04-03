package org.thaind.signaling.dto;

import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.thaind.signaling.cache.UserManager;
import org.thaind.signaling.exception.TooManyDeviceException;

import java.util.concurrent.TimeUnit;

/**
 * @author duyenthai
 */
public class UserConnection {

    private static final Timer TIMER = new HashedWheelTimer();

    private Channel webSocketChannel;
    private long lastTimeReceivePacket = System.currentTimeMillis();
    private final String userId;
    private Timeout timeoutPing;

    public UserConnection(String userId) throws TooManyDeviceException {
        this.userId = userId;
        createTimeoutPing(this);
        UserConnection res = UserManager.getInstance().addNewConnection(this);
        if (res != null) {
            throw new TooManyDeviceException();
        }
    }

    public void resPacket(Packet packet){
        // todo: logic send res packet
    }

    public Channel getWebSocketChannel() {
        return webSocketChannel;
    }

    public void setWebSocketChannel(Channel webSocketChannel) {
        this.webSocketChannel = webSocketChannel;
    }

    public long getLastTimeReceivePacket() {
        return lastTimeReceivePacket;
    }

    public void setLastTimeReceivePacket(long lastTimeReceivePacket) {
        this.lastTimeReceivePacket = lastTimeReceivePacket;
    }

    public String getUserId() {
        return userId;
    }

    private void createTimeoutPing(UserConnection userConnection) {
        if (timeoutPing != null) {
            timeoutPing.cancel();
        }
        timeoutPing = TIMER.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                // todo send ping
                boolean pingOk = false;
                if (pingOk) {
                    createTimeoutPing(userConnection);
                } else {
                    UserManager.getInstance().removeConnection(userConnection);
                }
            }
        }, 60, TimeUnit.SECONDS);
    }
}
