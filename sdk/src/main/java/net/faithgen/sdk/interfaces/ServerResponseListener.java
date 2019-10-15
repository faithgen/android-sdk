package net.faithgen.sdk.interfaces;

import net.faithgen.sdk.http.ErrorResponse;

public interface ServerResponseListener {
    void onResponse(String serverResponse);

    void onError(ErrorResponse errorResponse);
}
