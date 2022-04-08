package org.thaind.signaling.common;

/**
 * @author duyenthai
 */
public class MagicHolder {
    private int magic;
    private String msg;

    public MagicHolder(int magic, String msg) {
        this.magic = magic;
        this.msg = msg;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
