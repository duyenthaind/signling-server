package org.thaind.signaling.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.thaind.signaling.hibernate.HibernateUtil;
import org.thaind.signaling.hibernate.entity.UserEntity;
import org.thaind.signaling.repository.UserRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author duyenthai
 */
public class UserRepositoryImpl implements UserRepository {
    @Override
    public UserEntity findById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
            Root<UserEntity> rootObject = criteriaQuery.from(UserEntity.class);
            criteriaQuery.select(rootObject).where(criteriaBuilder.equal(rootObject.get("id"), id));
            Query<UserEntity> query = session.createQuery(criteriaQuery);
            List<UserEntity> list = query.getResultList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
        } catch (Exception ex) {
            LOGGER.error("Find contact by id error ", ex);
        }
        return null;
    }
}
