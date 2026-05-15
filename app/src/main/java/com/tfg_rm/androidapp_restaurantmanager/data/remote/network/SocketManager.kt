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

    // Estado para saber si debemos intentar reconectar
    private var isActive = false

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    suspend fun connect() {
        try {
            disconnect()
            isActive = true
            session = client.webSocketSession {
                url("${NetworkConfig.WS_URL}api/ws")
            }
            Log.i("SocketManager", "Conexión establecida correctamente")
        } catch (e: Exception) {
            Log.e("SocketManager", "Error al conectar: ${e.message}")
            handleReconnection()
        }
    }

    suspend fun sendMessage(message: String) {
        try {
            session?.send(Frame.Text(message))
            Log.i("SocketManager", "Mensaje enviado: $message")
        } catch (e: Exception) {
            Log.e("SocketManager", "No se pudo enviar el mensaje: ${e.message}")
        }
    }

    suspend fun listen() {
        while (isActive) { // Bucle infinito mientras queramos estar conectados
            try {
                session?.let { socketSession ->
                    for (frame in socketSession.incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            _messages.emit(text)
                        }
                    }
                }
                // Si el bucle termina sin excepción, es que el servidor cerró normal
                Log.w("SocketManager", "El servidor cerró la conexión.")
            } catch (e: Exception) {
                // Aquí cae el EOFException, ClosedReceiveChannelException, etc.
                Log.e("SocketManager", "Error en la escucha: ${e.message}")
            }

            // Si llegamos aquí, la conexión se ha perdido
            if (isActive) {
                handleReconnection()
            }
        }
    }

    private suspend fun handleReconnection() {
        Log.i("SocketManager", "Intentando reconectar en 5 segundos...")
        session = null
        kotlinx.coroutines.delay(5000) // Espera antes de reintentar para no saturar
        connect()
    }

    suspend fun disconnect() {
        isActive = false
        try {
            session?.close()
        } catch (e: Exception) {
            Log.e("SocketManager", "Error al cerrar sesión: ${e.message}")
        } finally {
            session = null
        }
    }
}