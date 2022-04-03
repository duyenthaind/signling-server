package org.thaind.signaling.hibernate;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class HibernateUtilTest {

    @Test
    void testMysql() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            NativeQuery<Object[]> query = session.createNativeQuery("show global status");
            List<Object[]> res = query.getResultList();
            assertNotEquals(0, res.size());
            res.forEach(val -> {
                Arrays.stream(val).forEach(System.out::print);
                System.out.println();
            });
        } finally {
            session.close();
        }
    }
}