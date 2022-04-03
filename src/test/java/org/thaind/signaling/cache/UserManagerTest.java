package org.thaind.signaling.cache;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class UserManagerTest {
    @Test
    void testPushAbsent() {
        Map<String, String> abc = new ConcurrentHashMap<>();
        String ok = abc.putIfAbsent("1", "b");
        String notOk = abc.putIfAbsent("1", "c");
        assertNotEquals(ok, "");
        assertNotEquals(notOk, "");
        System.out.println(ok);
        System.out.println(notOk);
    }
}