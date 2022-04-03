package org.thaind.signaling.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author duyenthai
 */
@Entity
@Table(name = "dh_room")
public class RoomEntity extends AbstractEntity implements Serializable {
    @Id
    private String id;
    @Column(name = "creator", nullable = false, length = 50)
    private String creator;
    @Column(name = "with_user", nullable = false, length = 50)
    private String withUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getWithUser() {
        return withUser;
    }

    public void setWithUser(String withUser) {
        this.withUser = withUser;
    }
}
