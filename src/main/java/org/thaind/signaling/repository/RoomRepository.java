package org.thaind.signaling.repository;

import org.thaind.signaling.hibernate.entity.RoomEntity;

/**
 * @author duyenthai
 */
public interface RoomRepository extends CRUDRepository<RoomEntity, String> {
    RoomEntity findByFromAndToUser(String fromUser, String toUser);
}
