package org.thaind.signaling.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.thaind.signaling.hibernate.HibernateUtil;
import org.thaind.signaling.hibernate.entity.MessageEntity;
import org.thaind.signaling.repository.MessageRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author duyenthai
 */
public class MessageRepositoryImpl implements MessageRepository {
    @Override
    public MessageEntity findById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<MessageEntity> criteriaQuery = criteriaBuilder.createQuery(MessageEntity.class);
            Root<MessageEntity> rootObject = criteriaQuery.from(MessageEntity.class);
            criteriaQuery.select(rootObject).where(criteriaBuilder.equal(rootObject.get("id"), id));
            Query<MessageEntity> query = session.createQuery(criteriaQuery);
            List<MessageEntity> list = query.getResultList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
        } catch (Exception ex) {
            LOGGER.error("Find contact by id error ", ex);
        }
        return null;
    }
}
