package net.faithgen.sdk.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utils {
    public static void openURL(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}
