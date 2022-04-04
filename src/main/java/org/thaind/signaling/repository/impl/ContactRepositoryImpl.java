package org.thaind.signaling.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.thaind.signaling.hibernate.HibernateUtil;
import org.thaind.signaling.hibernate.entity.ContactEntity;
import org.thaind.signaling.repository.ContactRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author duyenthai
 */
public class ContactRepositoryImpl implements ContactRepository {

    private static final Logger LOGGER = LogManager.getLogger("ContactRepositoryImpl");

    @Override
    public ContactEntity findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ContactEntity> criteriaQuery = criteriaBuilder.createQuery(ContactEntity.class);
            Root<ContactEntity> rootObject = criteriaQuery.from(ContactEntity.class);
            criteriaQuery.select(rootObject).where(criteriaBuilder.equal(rootObject.get("id"), id));
            Query<ContactEntity> query = session.createQuery(criteriaQuery);
            List<ContactEntity> list = query.getResultList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
        } catch (Exception ex) {
            LOGGER.error("Find contact by id error ", ex);
        }
        return null;
    }
}
