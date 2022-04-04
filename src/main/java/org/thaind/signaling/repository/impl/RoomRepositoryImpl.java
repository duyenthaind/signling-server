package org.thaind.signaling.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.thaind.signaling.hibernate.HibernateUtil;
import org.thaind.signaling.hibernate.entity.RoomEntity;
import org.thaind.signaling.repository.RoomRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author duyenthai
 */
public class RoomRepositoryImpl implements RoomRepository {
    @Override
    public RoomEntity findById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<RoomEntity> criteriaQuery = criteriaBuilder.createQuery(RoomEntity.class);
            Root<RoomEntity> rootObject = criteriaQuery.from(RoomEntity.class);
            criteriaQuery.select(rootObject).where(criteriaBuilder.equal(rootObject.get("id"), id));
            Query<RoomEntity> query = session.createQuery(criteriaQuery);
            List<RoomEntity> list = query.getResultList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
        } catch (Exception ex) {
            LOGGER.error("Find contact by id error ", ex);
        }
        return null;
    }
}
