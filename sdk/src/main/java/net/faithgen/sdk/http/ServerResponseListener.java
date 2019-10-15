package net.faithgen.sdk.http;

import com.android.volley.VolleyError;

import net.faithgen.sdk.singletons.GSONSingleton;
import net.faithgen.sdk.utils.Constants;

import java.io.UnsupportedEncodingException;

public interface ServerResponseListener {
    void onResponse(String serverResponse);

    void onError(ErrorResponse errorResponse);

    public static ErrorResponse makeErrorResponse(VolleyError error) {
        ErrorResponse errorResponse = null;
        if (error.networkResponse != null) {
            String responseBody = null;
            try {
                responseBody = new String(error.networkResponse.data, "utf-8");
                errorResponse = GSONSingleton.getInstance().getGson().fromJson(responseBody, ErrorResponse.class);
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
