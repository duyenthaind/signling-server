package org.thaind.signaling.dto;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.json.JSONObject;
import org.thaind.signaling.cache.RoomCallManager;
import org.thaind.signaling.common.Constants;
import org.thaind.signaling.dto.internal.protocol.Response;
import org.thaind.signaling.hibernate.entity.RoomEntity;
import org.thaind.signaling.repository.RoomRepository;
import org.thaind.signaling.repository.impl.RoomRepositoryImpl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author duyenthai
 */
public class CallRoom {

    private static final Timer TIMER = new HashedWheelTimer();

    private final List<UserConnection> userConnections = new CopyOnWriteArrayList<>();
    private final String roomId;
    private Timeout timeoutJoinRoom;
    private RoomEntity roomEntity;

    private final RoomRepository repository = new RoomRepositoryImpl();

    public CallRoom(String roomId) {
        this.roomId = roomId;
        roomEntity = repository.findById(roomId);
        RoomCallManager.getInstance().addNewRoom(this);
        createTimeoutJoinRoom(this);
    }

    public Response userJoinRoom(UserConnection userConnection) {
        if (roomEntity == null) {
            return Response.notFound();
        }
        if (!roomEntity.getCreator().equals(userConnection.getUserId().trim()) && !roomEntity.getWithUser().equals(userConnection.getUserId().trim())) {
            return Response.notPermitted("You have no permission to join this room");
        }
        userConnection.setCallRoom(this);
        notifyNewUserJoinRoom(userConnection);
        this.userConnections.add(userConnection);
        return Response.ok();
    }

    public void userLeaveRoom(UserConnection userConnection) {
        userConnections.remove(userConnection);
        notifyUserLeaveRoom(userConnection);
        RoomCallManager.getInstance().discardRoom(this);
        if (timeoutJoinRoom != null) {
            timeoutJoinRoom.cancel();
        }
    }

    public void sendPacketToOtherConnection(Packet packet, UserConnection userConnection) {
        if (!this.userConnections.contains(userConnection)) {
            return;
        }
        for (UserConnection connection : userConnections) {
            if (!userConnection.equals(connection) && !userConnection.getUserId().equals(connection.getUserId())) {
                connection.sendPacket(packet);
            }
        }
    }

    private void notifyNewUserJoinRoom(UserConnection connection) {
        Packet packet = new Packet();
        packet.setServiceType(Constants.PacketServiceType.USER_JOINED_ROOM);
        packet.setBody(new JSONObject());
        sendPacketToOtherConnection(packet, connection);
    }

    private void notifyUserLeaveRoom(UserConnection connection) {
        Packet packet = new Packet();
        packet.setServiceType(Constants.PacketServiceType.USER_LEAVE_ROOM_OR_TIMEOUT);
        packet.setBody(new JSONObject());
        sendPacketToOtherConnection(packet, connection);
    }

    private void notifyAllTimeout() {
        Packet packet = new Packet();
        packet.setServiceType(Constants.PacketServiceType.TIMEOUT_PICKUP);
        packet.setBody(new JSONObject());
        for (UserConnection connection : userConnections) {
            connection.sendPacket(packet);
        }
    }

    private void createTimeoutJoinRoom(CallRoom callRoom) {
        if (timeoutJoinRoom != null) {
            timeoutJoinRoom.cancel();
        }
        timeoutJoinRoom = TIMER.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                if (userConnections.size() != 2) {
                    notifyAllTimeout();
                    RoomCallManager.getInstance().discardRoom(callRoom);
                }
            }
        }, 3, TimeUnit.MINUTES);
    }

    public List<UserConnection> getUserConnections() {
        return userConnections;
    }

    public String getRoomId() {
        return roomId;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }
}
