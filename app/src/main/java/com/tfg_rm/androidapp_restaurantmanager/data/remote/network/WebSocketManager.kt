package com.tfg_rm.androidapp_restaurantmanager.data.remote.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    private val client: HttpClient
) {
    private val urlbase = "before-items-spending-dramatic.trycloudflare.com"
    private var session: DefaultClientWebSocketSession? = null
    private val _events = MutableSharedFlow<SocketEvent>()
    val events = _events.asSharedFlow()

    suspend fun connect() {
        try {
            session = client.webSocketSession {
                url("wss://$urlbase/api/ws")
            }
            Log.i("WebSocketManager", "WebSocket Conectado")
            listen()
        } catch (e: Exception) {
            Log.e("Socket", "Error conectando", e)
        }
    }

    private suspend fun listen() {
        try {
            session?.incoming?.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val text = frame.readText()

                    Log.i("WebSocketManager", "Response web socket: $text")

                    val event = parseEvent(text)

                    _events.emit(event)
                }
            }
        } catch (e: Exception) {
            Log.e("Socket", "Error escuchando", e)
        }
    }

    suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
    }

    suspend fun disconnect() {
        session?.close()
        session = null
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private fun parseEvent(text: String): SocketEvent {
        return try {
            val message = json.decodeFromString<SocketMessage>(text)

            when (message.type) {

                "CREATE_ORDER" -> {
                    SocketEvent.OrderCreated(message.payload)
                }

                "UPDATE_TABLE" -> {
                    SocketEvent.TableUpdated(message.payload)
                }

                "UPDATE_DISH" -> {
                    SocketEvent.DishUpdated(message.payload)
                }

                else -> {
                    Log.e(
                        "WebSocketManager",
                        "Evento desconocido: ${message.type}, contenido: ${message.payload}"
                    )
                    SocketEvent.Unknown
                }
            }

        } catch (e: Exception) {
            Log.e("WebSocketManager", "Error parseando", e)
            SocketEvent.Unknown
        }
    }
}

sealed class SocketEvent {
    data class OrderCreated(val data: JsonObject) : SocketEvent()
    data class TableUpdated(val data: JsonObject) : SocketEvent()
    data class DishUpdated(val data: JsonObject) : SocketEvent()
    data class EmployeeUpdated(val data: JsonObject) : SocketEvent()
    object Unknown : SocketEvent()
}

@Serializable
data class SocketMessage(
    val type: String,
    val payload: JsonObject
)