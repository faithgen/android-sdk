package net.faithgen.sdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import kotlinx.android.synthetic.main.activity_test_pusher.*

class TestPusher : AppCompatActivity() {

    private val authorizer: HttpAuthorizer by lazy { HttpAuthorizer("http://192.168.8.101:8001/laravel-websockets/auth") }

    private val pusherOptions: PusherOptions by lazy {
        authorizer.setHeaders(mapOf(Pair("X-App-ID", "myId")))

        PusherOptions()
                .setHost("192.168.8.101")
                .setWsPort(6001)
                .setWssPort(6001)
                .setAuthorizer(authorizer)
                .setEncrypted(false)
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
                e!!.printStackTrace()
                updateStatus(message!!, "Error")
            }
        }, ConnectionState.ALL)
    }

    private fun updateStatus(message: String, operation: String = "change") {
        statusText.text = message
        Log.d(operation, message)
    }
}
