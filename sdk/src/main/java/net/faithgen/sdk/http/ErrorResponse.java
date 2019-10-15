package net.faithgen.sdk.http;

public class ErrorResponse {
    private String message;
    private String code;
    private int status;

    public String getMessage() {
        return message.replace(".:", "\n");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
