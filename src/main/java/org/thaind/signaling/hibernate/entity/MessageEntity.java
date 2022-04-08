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
@Table(name = "dh_message")
public class MessageEntity extends AbstractEntity implements Serializable {

    @Id
    private String id;
    @Column(name = "conv_id", nullable = false)
    private String convId;
    @Column(name = "seq")
    private long seq;
    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;
    @Column(name = "from_user", nullable = false, length = 50)
    private String fromUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConvId() {
        return convId;
    }

    public void setConvId(String convId) {
        this.convId = convId;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

}
