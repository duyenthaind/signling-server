package org.thaind.signaling.cache;

import org.thaind.signaling.dto.UserConnection;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author duyenthai
 */
public class UserManager {

    private UserManager() {
    }

    private final ConcurrentHashMap<String, UserConnection> mapConnections = new ConcurrentHashMap<>();

    public UserConnection addNewConnection(UserConnection userConnection) {
        return mapConnections.put(userConnection.getUserId(), userConnection);
    }

    public void removeConnection(UserConnection userConnection) {
        mapConnections.remove(userConnection.getUserId());
    }

    public UserConnection getConnectionOfUser(String userId) {
        synchronized (mapConnections) {
            return mapConnections.get(userId);
        }
    }

    public static UserManager getInstance() {
        return Singleton.instance;
    }

    private static class Singleton {
        public static final UserManager instance = new UserManager();
    }
}
