package org.thaind.signaling.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author duyenthai
 */
@MappedSuperclass
public abstract class AbstractEntity {
    @Column(name = "createdAt")
    private long createdAt;
    @Column(name = "updatedAt")
    private long updatedAt;

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
