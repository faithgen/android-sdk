package net.faithgen.sdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import kotlinx.android.synthetic.main.activity_test_pusher.*
import java.lang.Exception

class TestPusher : AppCompatActivity() {

    private val authorizer: HttpAuthorizer by lazy { HttpAuthorizer("http://192.168.8.101:8001/laravel-websockets/auth") }

    private val pusherOptions : PusherOptions by lazy {
        authorizer.setHeaders(mapOf(Pair("X-App-ID", "myId")))

        PusherOptions()
                .setHost("http://192.168.8.101")
                .setWsPort(6001)
                .setWssPort(6001)
                .setAuthorizer(authorizer)
    }

    private val pusher: Pusher by lazy { Pusher("myKey", pusherOptions) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_pusher)

        connectSocket.setOnClickListener { connectPusher() }
    }

    private fun connectPusher() {
        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange?) {
                updateStatus("State changed to ${change!!.currentState} from ${change.previousState}")
            }

            override fun onError(message: String?, code: String?, e: Exception?) {
               updateStatus(message!!, "Error")
            }
        }, ConnectionState.ALL)
    }

    private fun updateStatus(message: String, operation : String = "change"){
        statusText.text = message
        Log.d(operation, message)
    }
}
