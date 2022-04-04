package org.thaind.signaling.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thaind.signaling.cache.ChatConversationManager;
import org.thaind.signaling.cache.UserConnectionManager;
import org.thaind.signaling.exception.TooManyDeviceException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author duyenthai
 */
public class UserConnection {

    private static final Timer TIMER = new HashedWheelTimer();
    private static final Logger LOGGER = LogManager.getLogger("UserConnection");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final boolean isForCall;

    private Channel webSocketChannel;
    private long lastTimeReceivePacket = System.currentTimeMillis();
    private String userId;
    private final Timeout timeoutAuthenticate;
    private CallRoom callRoom;
    private List<ChatConversation> listConversations = new CopyOnWriteArrayList<>();

    public UserConnection(boolean isForCall) {
        this.isForCall = isForCall;
        timeoutAuthenticate = TIMER.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                LOGGER.error("After 45s but receive no authentication packet, remove this connection " + webSocketChannel);
                webSocketChannel.writeAndFlush(new CloseWebSocketFrame());
                webSocketChannel.close();
            }
        }, 45, TimeUnit.SECONDS);
    }

    public void authenticate() throws TooManyDeviceException {
        //todo authenticate user, set userId
        if (timeoutAuthenticate != null) {
            timeoutAuthenticate.cancel();
        }
        UserConnection res = UserConnectionManager.getInstance().addNewConnection(this);
        if (res != null) {
            throw new TooManyDeviceException();
        }
        addToConversation();
    }

    public void sendPacket(Packet packet) {
        try {
            this.webSocketChannel.writeAndFlush(objectMapper.writeValueAsString(packet));
        } catch (Exception ex) {
            LOGGER.error("Error send packet", ex);
        }
    }

    public void disconnect() {
        try {
            LOGGER.info(String.format("Connection with channel %s, on disconnect", this.webSocketChannel));
            if (callRoom != null) {
                callRoom.userLeaveRoom(this);
            }
            if (webSocketChannel != null) {
                webSocketChannel.close();
            }
        } catch (Exception ex) {
            LOGGER.error("Remove connection error");
        } finally {
            listConversations.forEach(val -> val.removeConnection(this));
            UserConnectionManager.getInstance().removeConnection(this);
        }

    }

    private void addToConversation() {
        listConversations = ChatConversationManager.getInstance().getAllConversationOfUser(this.userId);
        listConversations.forEach(val -> val.addConnection(this));
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
