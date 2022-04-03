package org.thaind.signaling.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.thaind.signaling.common.Config;

import java.io.File;

/**
 * @author duyenthai
 */
public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY;

    private HibernateUtil() {
    }

    static {
        try {
            Configuration configuration = new Configuration();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .configure(new File(Config.CONFIG_FOLDER + File.separator + Config.HIBERNATE_CFG_FILE)).build();
            SESSION_FACTORY = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
}
