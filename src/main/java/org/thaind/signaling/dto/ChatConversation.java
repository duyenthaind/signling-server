package org.thaind.signaling.dto;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import org.thaind.signaling.hibernate.entity.ConversationEntity;
import org.thaind.signaling.repository.ConversationRepository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author duyenthai
 */
public class ChatConversation {

    private static final Timer TIMER = new HashedWheelTimer();

    private final List<UserConnection> userConnections = new CopyOnWriteArrayList<>();
    private final String conversationId;
    // after a time has no message, then remove this conv from RAM
    private Timeout timeoutRemoveConversation;
    private ConversationEntity conversationEntity;
    private long lastTimeNewMessage;

    //todo load conversation repo
    private final ConversationRepository repository = null;

    public ChatConversation(String conversationId) {
        this.conversationId = conversationId;
        conversationEntity = repository.findById(conversationId);
    }

    //todo chatconversation manager
}
