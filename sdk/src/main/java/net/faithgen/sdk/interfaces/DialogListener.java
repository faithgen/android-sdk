package net.faithgen.sdk.interfaces;

import android.util.Log;

public abstract class DialogListener {
    public abstract void onYes();

    public void onNope() {
        Log.d("TAG", "onNope: clicked");
    }
}