package net.faithgen.sdk.utils;

import android.app.Activity;
import android.content.Context;

import com.gdacciaro.iOSDialog.iOSDialogBuilder;

import net.faithgen.sdk.interfaces.DialogListener;

/**
 * This deals with system dialogs
 */
public final class Dialogs {
    /**
     * This shows a simple "oK" dialog
     *
     * @param context       The activity calling for a dialog
     * @param message       The message to be displayed
     * @param closeActivity Whether or not to close activity on ok
     */
    public static void showOkDialog(Context context, String message, boolean closeActivity) {
        new iOSDialogBuilder(context)
                .setTitle(Constants.SERVER_RESPONSE)
                .setSubtitle(message == null ? Constants.SERVER_ERROR : message)
                .setBoldPositiveLabel(true)
                .setCancelable(true)
                .setPositiveListener(Constants.OK, dialog -> {
                    dialog.dismiss();
                    if (closeActivity) {
                        Activity activity = (Activity) context;
                        activity.finish();
                    }
                })
                .build().show();
    }

    /**
     * This provides an Ok dialog with a callback to on ok
     * @param context
     * @param message
     * @param okDialogListener
     */
    public static void showOkDialog(Context context, String message, OkDialogListener okDialogListener) {
        new iOSDialogBuilder(context)
                .setTitle(Constants.SERVER_RESPONSE)
                .setSubtitle(message == null ? Constants.SERVER_ERROR : message)
                .setBoldPositiveLabel(true)
                .setCancelable(true)
                .setPositiveListener(Constants.OK, dialog -> {
                    dialog.dismiss();
                    if (okDialogListener != null)
                        okDialogListener.onOk();
                })
                .build().show();
    }

    /**
     * This opens a confirm dialog to the user
     *
     * @param context        The activity calling for this dialog
     * @param title          The title of the confirm request
     * @param message        The message to be shown to the user
     * @param dialogListener The listener to handle the user`s selection
     */
    public static void confirmDialog(Context context, String title, String message, DialogListener dialogListener) {
        new iOSDialogBuilder(context)
                .setTitle(title != null ? title : "Faith Gen")
                .setSubtitle(message)
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener(Constants.YES, dialog -> {
                    dialog.dismiss();
                    if (dialogListener != null)
                        dialogListener.onYes();
                })
                .setNegativeListener(Constants.NOPE, dialog -> {
                    dialog.dismiss();
                    if (dialogListener != null)
                        dialogListener.onNope();
                })
                .build()
                .show();
    }

    public interface OkDialogListener {
        void onOk();
    }
}
