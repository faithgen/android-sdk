package net.faithgen.sdk.http.types;

import android.content.Context;

import net.faithgen.sdk.http.ErrorResponse;
import net.faithgen.sdk.interfaces.ServerResponseListener;

import org.jetbrains.annotations.NotNull;

public abstract class ServerSilentResponse implements ServerResponseListener {

    public abstract void onServerResponse(String serverResponse);

    @Override
    public void onResponse(String serverResponse) {
        onServerResponse(serverResponse);
    }

    @Override
    public void onError(ErrorResponse errorResponse) {

    }

    @Override
    public void setUpCall(@NotNull Context context, boolean finish) {

    }
}
