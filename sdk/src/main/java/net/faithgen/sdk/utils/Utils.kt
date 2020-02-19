package net.faithgen.sdk.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object Utils {
    fun openURL(context: Context, url: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    @JvmOverloads
    fun shareText(context: Context, message: String?, subject: String? = "Subject") {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(Intent.createChooser(sharingIntent, Constants.SHARE_WITH))
    }

    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.packageName))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}