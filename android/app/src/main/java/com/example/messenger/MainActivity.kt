package com.example.messenger

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class MainActivity : AppCompatActivity() {

    lateinit var wsClient: WebSocketClient
    lateinit var chatOutput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val messageInput = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<Button>(R.id.sendButton)
        chatOutput = findViewById(R.id.chatOutput)

        val serverUri = URI("ws://192.168.1.7:3000") // Заміни на IP твого сервера
        wsClient = object : WebSocketClient(serverUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                runOnUiThread { chatOutput.append("Підключено до сервера\n") }
            }

            override fun onMessage(message: String?) {
                runOnUiThread { chatOutput.append("Інший: $message\n") }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                runOnUiThread { chatOutput.append("Відключено\n") }
            }

            override fun onError(ex: Exception?) {
                runOnUiThread { chatOutput.append("Помилка: ${ex?.message}\n") }
            }
        }
        wsClient.connect()

        sendButton.setOnClickListener {
            val text = messageInput.text.toString()
            if (text.isNotEmpty()) {
                wsClient.send(text)
                chatOutput.append("Я: $text\n")
                messageInput.text.clear()
            }
        }
    }

    override fun onDestroy() {
        wsClient.close()
        super.onDestroy()
    }
}
