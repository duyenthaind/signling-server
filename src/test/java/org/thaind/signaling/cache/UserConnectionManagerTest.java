package org.thaind.signaling.cache;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class UserConnectionManagerTest {
    @Test
    void testAllConnection() {
        Map<String, TestClass> map = new HashMap<>();
        TestClass o1 = new TestClass("ppp");
        map.put("1", o1);
        List<TestClass> l = new ArrayList<>(map.values());
        assertEquals(o1, l.get(0));
    }
}

class TestClass {
    private String id;

    public TestClass(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}