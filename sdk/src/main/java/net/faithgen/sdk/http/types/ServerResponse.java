package net.faithgen.sdk.http.types;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

import net.faithgen.sdk.http.ErrorResponse;
import net.faithgen.sdk.interfaces.ServerResponseListener;
import net.faithgen.sdk.utils.Constants;
import net.faithgen.sdk.utils.Dialogs;

public abstract class ServerResponse implements ServerResponseListener {

    private Context context;
    private boolean finishActivity;

    public abstract void onServerResponse(String serverResponse);

    public void setUpCall(Context context, boolean finishActivity) {
        this.context = context;
        this.finishActivity = finishActivity;
    }

    @Override
    public void onResponse(String serverResponse) {
        onServerResponse(serverResponse);
    }

    @Override
    public void onError(ErrorResponse errorResponse) {
        Dialogs.showOkDialog(getContext(), errorResponse != null ? errorResponse.getMessage() : Constants.SERVER_ERROR, isFinishActivity());
    }

    public Context getContext() {
        return context;
    }

    public boolean isFinishActivity() {
        return finishActivity;
    }
}
