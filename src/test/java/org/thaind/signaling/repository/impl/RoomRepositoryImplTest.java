package org.thaind.signaling.repository.impl;

import org.junit.jupiter.api.Test;
import org.thaind.signaling.hibernate.entity.RoomEntity;
import org.thaind.signaling.repository.RoomRepository;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class RoomRepositoryImplTest {
    private static final RoomRepository REPOSITORY = new RoomRepositoryImpl();

    @Test
    void testSave(){
        RoomEntity entity = new RoomEntity();
        entity.setId("6cf4fee1-e552-4ed2-9f54-19055f0a3b28");
        entity.setCreator("A");
        entity.setWithUser("B");
        RoomEntity e = REPOSITORY.saveOrUpdate(entity);
        assertNotNull(e);
        System.out.println(e);
    }

    @Test
    void testFetch(){
        RoomEntity e = REPOSITORY.findById("6cf4fee1-e552-4ed2-9f54-19055f0a3b28");
        assertNotNull(e);
        System.out.println(e);
    }
}