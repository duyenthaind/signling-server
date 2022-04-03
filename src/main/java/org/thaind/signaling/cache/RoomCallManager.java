package org.thaind.signaling.cache;

/**
 * @author duyenthai
 */

import org.thaind.signaling.dto.CallRoom;
import org.thaind.signaling.dto.UserConnection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is used to manage room
 */
public class RoomCallManager {

    private static final Map<String, CallRoom> ROOMS = new ConcurrentHashMap<>();

    private RoomCallManager() {
    }

    public CallRoom addNewRoom(CallRoom callRoom) {
        return ROOMS.putIfAbsent(callRoom.getRoomId(), callRoom);
    }

    public void discardRoom(CallRoom callRoom) {
        synchronized (ROOMS) {
            CallRoom room = ROOMS.get(callRoom.getRoomId());
            if (room != null) {
                ROOMS.remove(callRoom.getRoomId());
                room.getUserConnections().forEach(UserConnection::disconnect);
            }
        }
    }

    public CallRoom getCallRoom(String roomId) {
        return ROOMS.get(roomId);
    }

    public static RoomCallManager getInstance() {
        return RoomCallManager.Singleton.instance;
    }

    private static class Singleton {
        public static final RoomCallManager instance = new RoomCallManager();
    }
}
