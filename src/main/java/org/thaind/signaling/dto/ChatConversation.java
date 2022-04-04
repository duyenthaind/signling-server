package org.thaind.signaling.dto;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.thaind.signaling.cache.ChatConversationManager;
import org.thaind.signaling.dto.internal.protocol.Response;
import org.thaind.signaling.hibernate.entity.ConversationEntity;
import org.thaind.signaling.repository.ConversationRepository;
import org.thaind.signaling.repository.impl.ConversationRepositoryImpl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;


/**
 * @author duyenthai
 */
public class ChatConversation {

    private static final Timer TIMER = new HashedWheelTimer();

    private static final long DAY_TO_SECONDS = 24 * 60 * 60;

    private final List<UserConnection> userConnections = new CopyOnWriteArrayList<>();
    private final String conversationId;
    // after a time has no message, then remove this conv from RAM
    private Timeout timeoutRemoveConversation;
    private ConversationEntity conversationEntity;
    private long lastTimeNewMessage;

    private final ConversationRepository repository = new ConversationRepositoryImpl();

    public ChatConversation(String conversationId) {
        this.conversationId = conversationId;
        conversationEntity = repository.findById(conversationId);
        ChatConversationManager.getInstance().addNewChatConversation(this);
        lastTimeNewMessage = System.currentTimeMillis() / 1000L;
        createTimeoutRemoveConversation(this);
    }

    public Response addConnection(UserConnection userConnection) {
        if (conversationEntity == null) {
            return Response.notFound();
        }
        if (userConnection.getUserId().equals(conversationEntity.getCreator()) || userConnection.getUserId().equals(conversationEntity.getWithUser())) {
            userConnections.add(userConnection);
            return Response.ok();
        } else {
            return Response.notPermitted("You do not have permission to join this conversation");
        }
    }

    public void removeConnection(UserConnection userConnection) {
        userConnections.remove(userConnection);
        userConnections.forEach(val -> {
            if (val.getUserId().equals(userConnection.getUserId())) {
                userConnections.remove(val);
            }
        });
    }

    private void createTimeoutRemoveConversation(ChatConversation chatConversation) {
        if (timeoutRemoveConversation != null) {
            timeoutRemoveConversation.cancel();
        }
        timeoutRemoveConversation = TIMER.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                if (System.currentTimeMillis() / 1000L - lastTimeNewMessage >= DAY_TO_SECONDS) {
                    ChatConversationManager.getInstance().removeConversation(chatConversation);
                }
            }
        }, DAY_TO_SECONDS, TimeUnit.SECONDS);
    }

    public void sendPacketToOtherConnection(Packet packet, UserConnection userConnection) {
        if (this.userConnections.contains(userConnection)) {
            return;
        }
        for (UserConnection connection : userConnections) {
            if (!userConnection.equals(connection) && !userConnection.getUserId().equals(connection.getUserId())) {
                connection.sendPacket(packet);
            }
        }
    }

    public String getConversationId() {
        return conversationId;
    }

    public ConversationEntity getConversationEntity() {
        return conversationEntity;
    }

    public void setConversationEntity(ConversationEntity conversationEntity) {
        this.conversationEntity = conversationEntity;
    }

    public long getLastTimeNewMessage() {
        return lastTimeNewMessage;
    }

    public void setLastTimeNewMessage(long lastTimeNewMessage) {
        this.lastTimeNewMessage = lastTimeNewMessage;
    }
}
