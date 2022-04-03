package org.thaind.signaling.dto;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.thaind.signaling.cache.RoomCallManager;
import org.thaind.signaling.dto.internal.protocol.Response;
import org.thaind.signaling.hibernate.entity.RoomEntity;
import org.thaind.signaling.repository.RoomRepository;

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

    // todo load room repository
    private final RoomRepository repository = null;

    public CallRoom(String roomId) {
        this.roomId = roomId;
        roomEntity = repository.findById(roomId);
        RoomCallManager.getInstance().addNewRoom(this);
        createTimeoutJoinRoom(this);
    }

    public Response userJoinRoom(UserConnection userConnection, String roomToken) {
        if (roomEntity == null) {
            return Response.notFound();
        }
        //todo parse roomToken first
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

    public void notifyPacketToAllUser(Packet packet) {
        for (UserConnection connection : userConnections) {
            connection.sendPacket(packet);
        }
    }

    private void notifyNewUserJoinRoom(UserConnection connection) {
        // todo notify new user join packet
    }

    private void notifyUserLeaveRoom(UserConnection connection) {
        // todo notify new user lave packet
    }

    private void notifyAllTimeout() {
        // todo timeout all but user not pickup
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
