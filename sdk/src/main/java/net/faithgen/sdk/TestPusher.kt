package net.faithgen.sdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test_pusher.*

class TestPusher : AppCompatActivity() {

    //private val pusherOptions : PusherO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_pusher)

        connectSocket.setOnClickListener { connectPusher() }
    }

    private fun connectPusher() {

    }
}
