package org.thaind.signaling.dto;

import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.thaind.signaling.cache.UserConnectionManager;
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
    private final boolean isForCall;
    private CallRoom callRoom;

    public UserConnection(String userId, boolean isForCall) throws TooManyDeviceException {
        this.userId = userId;
        this.isForCall = isForCall;
        createTimeoutPing(this);
        UserConnection res = UserConnectionManager.getInstance().addNewConnection(this);
        if (res != null) {
            throw new TooManyDeviceException();
        }
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
                    userConnection.disconnect();
                }
            }
        }, 60, TimeUnit.SECONDS);
    }

    public void sendPacket(Packet packet) {
        // todo: logic send res packet
    }

    public void disconnect() {
        UserConnectionManager.getInstance().removeConnection(this);
        if (webSocketChannel != null) {
            webSocketChannel.close();
        }
        if (timeoutPing != null) {
            timeoutPing.cancel();
        }
        if (callRoom != null) {
            callRoom.userLeaveRoom(this);
        }
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

    public boolean isForCall() {
        return isForCall;
    }

    public CallRoom getCallRoom() {
        return callRoom;
    }

    public void setCallRoom(CallRoom callRoom) {
        if (!this.isForCall) {
            return;
        }
        this.callRoom = callRoom;
    }
}
