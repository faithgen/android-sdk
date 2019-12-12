package net.faithgen.sdk.http;

import net.faithgen.sdk.models.Comment;

public class Response<T> {
    private T data;
    private String message;
    private boolean success;
    private Comment comment;

    public Comment getComment() {
        return comment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
