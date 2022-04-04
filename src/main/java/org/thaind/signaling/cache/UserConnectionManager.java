package org.thaind.signaling.cache;

import org.apache.commons.lang3.StringUtils;
import org.thaind.signaling.dto.UserConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author duyenthai
 */

/**
 * This class is used to manage common connection from user
 * all the connection is for messaging, making call purpose
 */
public class UserConnectionManager {

    private static final String FOR_CALL_KEY_FORMAT = "%s_for_call";

    private UserConnectionManager() {
    }

    private final ConcurrentHashMap<String, UserConnection> mapConnections = new ConcurrentHashMap<>();

    public UserConnection addNewConnection(UserConnection userConnection) {
        if (!userConnection.isForCall()) {
            return mapConnections.putIfAbsent(userConnection.getUserId(), userConnection);
        } else {
            return mapConnections.putIfAbsent(String.format(FOR_CALL_KEY_FORMAT, userConnection.getUserId()), userConnection);
        }
    }

    public void removeConnection(UserConnection userConnection) {
        if (StringUtils.isEmpty(userConnection.getUserId())) {
            return;
        }
        if (!userConnection.isForCall()) {
            mapConnections.remove(userConnection.getUserId());
        } else {
            mapConnections.remove(String.format(FOR_CALL_KEY_FORMAT, userConnection.getUserId()));
        }
    }

    public UserConnection getConnectionOfUser(String userId) {
        synchronized (mapConnections) {
            return mapConnections.get(userId);
        }
    }

    // todo test this function, if connection is not change to new object
    public List<UserConnection> getAllConnections() {
        return new ArrayList<>(mapConnections.values());
    }

    public static UserConnectionManager getInstance() {
        return Singleton.instance;
    }

    private static class Singleton {
        public static final UserConnectionManager instance = new UserConnectionManager();
    }
}
