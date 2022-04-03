package org.thaind.signaling.dto.internal.protocol;

import org.thaind.signaling.common.Constants;

/**
 * @author duyenthai
 */
public class Response {
    private int res;
    private String message;

    public Response() {
    }

    public Response(int res, String message) {
        this.res = res;
        this.message = message;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Response notPermitted(String msg) {
        return new Response(Constants.ResponseType.NOT_PERMITTED.getRes(), msg);
    }

    public static Response ok() {
        return new Response(Constants.ResponseType.OK.getRes(), "Ok");
    }

    public static Response notFound() {
        return new Response(Constants.ResponseType.NOT_FOUND.getRes(), "Not found");
    }
}
