package org.thaind.signaling.repository;

import org.thaind.signaling.hibernate.entity.MessageEntity;

import java.util.List;

/**
 * @author duyenthai
 */
public interface MessageRepository extends CRUDRepository<MessageEntity, String> {
    List<MessageEntity> getMessageFromConversation(String conversationId, int fromSeq, int amount);
}
