package net.faithgen.sdk.interfaces

import android.util.Log

abstract class DialogListener {
    abstract fun onYes()
    fun onNope() {
        Log.d("TAG", "onNope: clicked")
    }
}