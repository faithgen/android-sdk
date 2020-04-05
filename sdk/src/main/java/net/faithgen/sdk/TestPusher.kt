package net.faithgen.sdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.channel.PusherEvent
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import kotlinx.android.synthetic.main.activity_test_pusher.*

class TestPusher : AppCompatActivity() {

    private val authorizer: HttpAuthorizer by lazy { HttpAuthorizer("http://192.168.8.101:8001/laravel-websockets/auth") }

    private val channels: List<String> = listOf(
            "disconnection",
            "connection",
            "vacated",
            "occupied",
            "subscribed",
            "client-message",
            "api-message"
    )

    private var status = ""

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

        //subscribeToAllChannels()

        val commentsChannel = pusher.subscribePrivate("private-comments-sermons--5-3e4c7f160-b5191986fb06-be8fe23ad2")

        commentsChannel.bind("comment.created", object : PrivateChannelEventListener {
            override fun onEvent(event: PusherEvent?) {
                updateStatus("FROM MY EVENT")
                updateStatus(event!!.data, "From Event message")
            }

            override fun onAuthenticationFailure(message: String?, e: java.lang.Exception?) {
                TODO("Not yet implemented")
            }

            override fun onSubscriptionSucceeded(channelName: String?) {
                updateStatus("Subsrcibed custom successfully")
            }
        })

    }

    private fun subscribeToAllChannels() {
        channels.forEach { channel -> subscribeToChannel(channel) }
    }

    private fun subscribeToChannel(pusherChannel: String) {
        val channel: PrivateChannel = pusher.subscribePrivate("private-websockets-dashboard-$pusherChannel")
        channel.bind("log-message", object : PrivateChannelEventListener {
            override fun onEvent(event: PusherEvent?) {
                updateStatus(event.toString(), pusherChannel)
            }

            override fun onAuthenticationFailure(message: String?, e: java.lang.Exception?) {
                updateStatus(message!!, "failed auth")
            }

            override fun onSubscriptionSucceeded(channelName: String?) {
                updateStatus("Subscription passed", pusherChannel)
            }
        })
    }

    private fun updateStatus(message: String, operation: String = "change") {
        status += message + "\n"
        runOnUiThread { statusText.text = status }
        Log.d(operation, message)
    }
}
