package org.thaind.signaling.repository;

import org.thaind.signaling.hibernate.entity.ConversationEntity;

import java.util.Optional;

/**
 * @author duyenthai
 */
public interface ConversationRepository extends CRUDRepository<ConversationEntity, String> {
    Optional<ConversationEntity> findByCreatorAndWithUser(String creator, String withUser);
}
