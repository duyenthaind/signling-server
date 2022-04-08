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
@Table(name = "dh_conversation_user")
public class ConversationUserEntity extends AbstractEntity implements Serializable {
    @Id
    private String id;
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;
    @Column(name = "conv_id")
    private String convId;
    @Column(name = "last_seq")
    private long lastSeq;
    @Column(name = "last_seq_seen")
    private long lastSeqSeen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConvId() {
        return convId;
    }

    public void setConvId(String convId) {
        this.convId = convId;
    }

    public long getLastSeq() {
        return lastSeq;
    }

    public void setLastSeq(long lastSeq) {
        this.lastSeq = lastSeq;
    }

    public long getLastSeqSeen() {
        return lastSeqSeen;
    }

    public void setLastSeqSeen(long lastSeqSeen) {
        this.lastSeqSeen = lastSeqSeen;
    }
}
