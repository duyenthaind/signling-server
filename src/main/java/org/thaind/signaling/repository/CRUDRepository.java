package org.thaind.signaling.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.thaind.signaling.hibernate.HibernateUtil;
import org.thaind.signaling.hibernate.entity.AbstractEntity;

/**
 * @author duyenthai
 */
public interface CRUDRepository<T extends AbstractEntity, ID> {

    Logger LOGGER = LogManager.getLogger("CRUDRepository");

    T findById(ID id);

    default T save(T t) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.save(t);
            session.getTransaction().commit();
        } catch (Exception ex) {
            LOGGER.error("Save error ", ex);
        }
        return t;
    }

    default T update(T t) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.update(t);
            session.getTransaction().commit();
        } catch (Exception ex) {
            LOGGER.error("Update error ", ex);
        }
        return t;
    }

    default T delete(T t) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.delete(t);
            session.getTransaction().commit();
        } catch (Exception ex) {
            LOGGER.error("Delete error ", ex);
        }
        return t;
    }

    default T saveOrUpdate(T t) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.saveOrUpdate(t);
            session.getTransaction().commit();
        } catch (Exception ex) {
            LOGGER.error("Save error ", ex);
        }
        return t;
    }
}
