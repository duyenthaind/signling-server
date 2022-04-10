package org.thaind.signaling.repository.impl;

import org.junit.jupiter.api.Test;
import org.thaind.signaling.hibernate.entity.UserEntity;
import org.thaind.signaling.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class UserRepositoryImplTest {
    UserRepository repository = new UserRepositoryImpl();

    @Test
    void test(){
        UserEntity u = repository.findById("id111111111");
        System.out.println(u);
        assertTrue(true);
    }
}