package com.tfg_rm.androidapp_restaurantmanager.data.remote.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor(
    private val client: HttpClient
) {
    private var session: DefaultClientWebSocketSession? = null

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    suspend fun connect() {
        disconnect()
        session = client.webSocketSession {
            url("${NetworkConfig.WS_URL}api/ws")
        }
        Log.i("SocketManager", "SocketManager conexion realizada")
    }

    suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
        Log.i("SocketManager", "Mensaje enviado: $message")
    }

    suspend fun listen() {
        session?.let { socketSession ->
            for (frame in socketSession.incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        Log.i("SocketManager", "Recibido en SocketManager: $text")
                        _messages.emit(text)
                    }

                    else -> {}
                }
            }
        }
    }

    suspend fun disconnect() {
        session?.let {
            it.close()
            session = null
            Log.i("SocketManager", "SocketManager websockets desconectados")
        }
    }
}