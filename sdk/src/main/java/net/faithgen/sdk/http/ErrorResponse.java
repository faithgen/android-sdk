package net.faithgen.sdk.http;

import com.android.volley.VolleyError;

import net.faithgen.sdk.singletons.GSONSingleton;
import net.faithgen.sdk.utils.Constants;

import java.io.UnsupportedEncodingException;

/**
 * The error response sent to the system for every failed HTTP call
 */
final public class ErrorResponse {
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


    /**
     * This makes an error response from a Volley error
     * @param error
     * @return
     */
    public static ErrorResponse makeErrorResponse(VolleyError error) {
        ErrorResponse errorResponse = null;
        if (error.networkResponse != null) {
            String responseBody = null;
            try {
                responseBody = new String(error.networkResponse.data, "utf-8");
                errorResponse = GSONSingleton.Companion.getInstance().getGson().fromJson(responseBody, ErrorResponse.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            errorResponse = new ErrorResponse();
            errorResponse.setCode("SER");
            errorResponse.setStatus(500);
            errorResponse.setMessage(Constants.SERVER_ERROR);
        }
        return errorResponse;
    }
}
