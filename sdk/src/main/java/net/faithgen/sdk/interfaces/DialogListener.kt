package net.faithgen.sdk.interfaces

import android.util.Log

/**
 * This handles confirm dialogs
 */
abstract class DialogListener {
    /**
     * The callback to take place when a user confirms
     */
    abstract fun onYes()

    /**
     * Override this if you want to do something else
     * when a user declines what you are confirming
     */
    fun onNope() {
        Log.d("TAG", "onNope: clicked")
    }
}