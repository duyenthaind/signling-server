package org.thaind.signaling.repository.impl;

import org.junit.jupiter.api.Test;
import org.thaind.signaling.hibernate.entity.MessageEntity;
import org.thaind.signaling.repository.MessageRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthai
 */
class MessageRepositoryImplTest {

    @Test
    void getMessageFromConversation() {
        MessageRepository repository = new MessageRepositoryImpl();
        List<MessageEntity> list = repository.getMessageFromConversation("conv1",0,10);
        System.out.println(list);
        assertNotEquals(0, list.size());
    }

    @Test
    void getMessageFromConversation2() {
        MessageRepository repository = new MessageRepositoryImpl();
        List<MessageEntity> list = repository.getMessageFromConversation("conv2",0,10);
        System.out.println(list);
        assertEquals(0, list.size());
    }
}