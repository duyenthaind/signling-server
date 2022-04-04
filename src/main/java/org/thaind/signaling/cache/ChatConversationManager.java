package org.thaind.signaling.cache;

import org.thaind.signaling.dto.ChatConversation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author duyenthai
 */
public class ChatConversationManager {

    private static final Map<String, ChatConversation> CONVERSATIONS = new ConcurrentHashMap<>();

    private ChatConversationManager() {
    }

    public ChatConversation addNewChatConversation(ChatConversation chatConversation) {
        return CONVERSATIONS.putIfAbsent(chatConversation.getConversationId(), chatConversation);
    }

    public ChatConversation getConversation(String conversationId) {
        return CONVERSATIONS.get(conversationId);
    }

    public void removeConversation(ChatConversation conversation) {
        CONVERSATIONS.remove(conversation.getConversationId());
    }

    public List<ChatConversation> getAllConversationOfUser(String userId) {
        List<ChatConversation> list = new CopyOnWriteArrayList<>();
        for (ChatConversation index : CONVERSATIONS.values()) {
            if (index.getConversationEntity().getCreator().equals(userId) || index.getConversationEntity().getWithUser().equals(userId)) {
                list.add(index);
            }
        }
        return list;
    }

    public static ChatConversationManager getInstance() {
        return Singleton.instance;
    }

    private static class Singleton {
        public static final ChatConversationManager instance = new ChatConversationManager();
    }
}
