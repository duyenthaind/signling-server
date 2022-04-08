package org.thaind.signaling.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.thaind.signaling.hibernate.HibernateUtil;
import org.thaind.signaling.hibernate.entity.ConversationUserEntity;
import org.thaind.signaling.repository.ConversationUserRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * @author duyenthai
 */
public class ConversationUserRepositoryImpl implements ConversationUserRepository {
    @Override
    public ConversationUserEntity findById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ConversationUserEntity> criteriaQuery = criteriaBuilder.createQuery(ConversationUserEntity.class);
            Root<ConversationUserEntity> rootObject = criteriaQuery.from(ConversationUserEntity.class);
            criteriaQuery.select(rootObject).where(criteriaBuilder.equal(rootObject.get("id"), id));
            Query<ConversationUserEntity> query = session.createQuery(criteriaQuery);
            List<ConversationUserEntity> list = query.getResultList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
        } catch (Exception ex) {
            LOGGER.error("Find contact by id error ", ex);
        }
        return null;
    }

    @Override
    public Optional<ConversationUserEntity> findByUserIdAndConversationId(String userId, String convId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from ConversationUserEntity where userId = :userId and convId = :convId";
            Query<ConversationUserEntity> query = session.createQuery(hql)
                    .setParameter("userId", userId)
                    .setParameter("convId", convId);
            List<ConversationUserEntity> list = query.list();
            if (!list.isEmpty()) {
                return Optional.of(list.get(0));
            }
        } catch (Exception ex) {
            LOGGER.error("Find conversation user by userid and convid error ", ex);
        }
        return Optional.empty();
    }
}
