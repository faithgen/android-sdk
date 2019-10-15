package net.faithgen.sdk.utils;

import android.app.Activity;
import android.content.Context;

import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;

import net.faithgen.sdk.interfaces.DialogListener;


public class Progress {
    public static KProgressHUD progressHUD;

    public static KProgressHUD getProgressHUD() {
        return progressHUD;
    }

    public static void showProgress(Context context, String message) {
        progressHUD = KProgressHUD.create(context)
                .setAutoDismiss(false)
                .setCancellable(false)
                .setLabel(message == null ? Constants.PLEASE_WAIT : message)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .show();
    }

    public static void dismissProgress() {
        getProgressHUD().dismiss();
    }
}
