package net.faithgen.sdk.http.types;

import net.faithgen.sdk.http.ErrorResponse;
import net.faithgen.sdk.http.ServerResponseListener;

public abstract class ServerSilentResponse implements ServerResponseListener {

    public abstract void onServerResponse(String serverResponse);

    @Override
    public void onResponse(String serverResponse) {
        onServerResponse(serverResponse);
    }

    @Override
    public void onError(ErrorResponse errorResponse) {

    }
}
