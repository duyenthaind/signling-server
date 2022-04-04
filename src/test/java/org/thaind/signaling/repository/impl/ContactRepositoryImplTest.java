package org.thaind.signaling.repository.impl;

import org.junit.jupiter.api.Test;
import org.thaind.signaling.hibernate.entity.ContactEntity;
import org.thaind.signaling.repository.ContactRepository;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class ContactRepositoryImplTest {

    private static final ContactRepository REPOSITORY = new ContactRepositoryImpl();

    @Test
    void testSaveOrUpdate(){
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setCreator("A");
        contactEntity.setContactId("B");
        ContactEntity e = REPOSITORY.saveOrUpdate(contactEntity);
        assertNotNull(e);
    }

    @Test
    void testFetch(){
        long id = 1;
        ContactEntity contactEntity = REPOSITORY.findById(id);
        System.out.println(contactEntity);
        assertNotNull(contactEntity);
    }
}