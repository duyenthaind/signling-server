package org.thaind.signaling.dto;

import com.corundumstudio.socketio.SocketIOClient;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.thaind.signaling.cache.ChatConversationManager;
import org.thaind.signaling.cache.UserConnectionManager;
import org.thaind.signaling.common.Constants;
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

    private boolean isForCall;

    // webSocket
    private Channel webSocketChannel;
    // socket IO
    private SocketIOClient socketIOClient;
    private long lastTimeReceivePacket = System.currentTimeMillis();
    private String userId;
    private final Timeout timeoutAuthenticate;
    private CallRoom callRoom;
    private List<ChatConversation> listConversations = new CopyOnWriteArrayList<>();

    public UserConnection() {
        UserConnection this1 = this;
        timeoutAuthenticate = TIMER.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                this1.disconnect();

            }
        }, 45, TimeUnit.SECONDS);
    }

    public void authenticate() throws TooManyDeviceException {
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
            if (this.webSocketChannel != null) {
                JSONObject res = new JSONObject();
                res.put("service", packet.getServiceType().getServiceType());
                res.put("body", packet.getBody());
                this.webSocketChannel.writeAndFlush(new TextWebSocketFrame(res.toString()));
            } else if (this.socketIOClient != null) {
                this.socketIOClient.sendEvent(Constants.SocketIoEvent.EVENT_PACKET.getEvent(), new EventPacket(packet.getServiceType().getServiceType(), packet.getBodyString()));
            }
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
            if (socketIOClient != null) {
                socketIOClient.disconnect();
            }
        } catch (Exception ex) {
            LOGGER.error("Remove connection error");
        } finally {
            listConversations.forEach(val -> val.removeConnection(this));
            UserConnectionManager.getInstance().removeConnection(this);
        }

    }

    public void userJoinConversation(ChatConversation conversation) {
        if (!listConversations.contains(conversation)) {
            listConversations.add(conversation);
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

    public SocketIOClient getSocketIOClient() {
        return socketIOClient;
    }

    public void setSocketIOClient(SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public void setForCall(boolean forCall) {
        isForCall = forCall;
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
