package org.thaind.signaling.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.thaind.signaling.hibernate.HibernateUtil;
import org.thaind.signaling.hibernate.entity.ConversationEntity;
import org.thaind.signaling.repository.ConversationRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * @author duyenthai
 */
public class ConversationRepositoryImpl implements ConversationRepository {
    @Override
    public ConversationEntity findById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ConversationEntity> criteriaQuery = criteriaBuilder.createQuery(ConversationEntity.class);
            Root<ConversationEntity> rootObject = criteriaQuery.from(ConversationEntity.class);
            criteriaQuery.select(rootObject).where(criteriaBuilder.equal(rootObject.get("id"), id));
            Query<ConversationEntity> query = session.createQuery(criteriaQuery);
            List<ConversationEntity> list = query.getResultList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
        } catch (Exception ex) {
            LOGGER.error("Find contact by id error ", ex);
        }
        return null;
    }

    @Override
    public Optional<ConversationEntity> findByCreatorAndWithUser(String creator, String withUser) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ConversationEntity> criteriaQuery = criteriaBuilder.createQuery(ConversationEntity.class);
            Root<ConversationEntity> rootObject = criteriaQuery.from(ConversationEntity.class);
            Predicate predicateCreator = criteriaBuilder.equal(rootObject.get("creator"), creator);
            Predicate predicateWithUser = criteriaBuilder.equal(rootObject.get("withUser"), withUser);
            criteriaQuery.select(rootObject)
                    .where(criteriaBuilder.and(predicateCreator, predicateWithUser));
//                    .where(criteriaBuilder.equal(rootObject.get("withUser"), withUser));
            Query<ConversationEntity> query = session.createQuery(criteriaQuery);
            List<ConversationEntity> list = query.getResultList();
            if (!list.isEmpty()) {
                return Optional.of(list.get(0));
            }
        } catch (Exception ex) {
            LOGGER.error("Find by creator and with user error ", ex);
        }
        return Optional.empty();
    }
}
