package org.thaind.signaling.repository.impl;

import org.junit.jupiter.api.Test;
import org.thaind.signaling.hibernate.entity.ConversationEntity;
import org.thaind.signaling.repository.ConversationRepository;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class ConversationRepositoryImplTest {
    private static final ConversationRepository REPOSITORY = new ConversationRepositoryImpl();

    @Test
    void testSave(){
        ConversationEntity entity = new ConversationEntity();
        entity.setId("6cf4fee1-e552-4ed2-9f54-19055f0a3b28");
        entity.setCreator("A");
        entity.setWithUser("B");
        ConversationEntity e = REPOSITORY.saveOrUpdate(entity);
        assertNotNull(e);
        System.out.println(e);
    }

    @Test
    void testFetch(){
        ConversationEntity e = REPOSITORY.findById("6cf4fee1-e552-4ed2-9f54-19055f0a3b28");
        assertNotNull(e);
        System.out.println(e);
    }
}