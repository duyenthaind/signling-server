package org.thaind.signaling.repository;

import org.thaind.signaling.hibernate.entity.ConversationUserEntity;

import java.util.Optional;

/**
 * @author duyenthai
 */
public interface ConversationUserRepository extends CRUDRepository<ConversationUserEntity, String>{

    Optional<ConversationUserEntity> findByUserIdAndConversationId(String userId, String convId);
}
