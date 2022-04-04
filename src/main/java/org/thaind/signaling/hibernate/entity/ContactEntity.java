package org.thaind.signaling.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author duyenthai
 */

@Entity
@Table(name = "dh_contact")
public class ContactEntity extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "creator", nullable = false, length = 50)
    private String creator;
    @Column(name = "contact_id", nullable = false, length = 50)
    private String contactId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @Override
    public String toString() {
        return "ContactEntity{" +
                "id=" + id +
                ", creator='" + creator + '\'' +
                ", contactId='" + contactId + '\'' +
                '}';
    }
}
