package app.pixroost.desktop.spike

import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

/** A tiny HTTP listener on all interfaces, so a phone browser can prove the firewall rule works. */
class ConnectivityServer {
    private var serverSocket: ServerSocket? = null
    private val counter = AtomicInteger()

    val requests: Int get() = counter.get()

    /** Returns null on success or the error text. */
    fun start(): String? = try {
        val socket = ServerSocket(SpikeConstants.LISTEN_PORT)
        serverSocket = socket
        thread(isDaemon = true, name = "s07-listener") { acceptLoop(socket) }
        null
    } catch (e: IOException) {
        "cannot listen on ${SpikeConstants.LISTEN_PORT}: ${e.message}"
    }

    fun stop() {
        serverSocket?.close()
    }

    private fun acceptLoop(socket: ServerSocket) {
        while (!socket.isClosed) {
            try {
                socket.accept().use { answer(it) }
            } catch (e: IOException) {
                System.err.println("s07 listener: ${e.message}")
            }
        }
    }

    private fun answer(client: Socket) {
        client.getInputStream().bufferedReader().readLine()
        counter.incrementAndGet()
        val body = "<html><meta charset=\"utf-8\"><body><h1>${SpikeConstants.PAGE_TEXT}</h1></body></html>"
        val bytes = body.toByteArray(Charsets.UTF_8)
        val header = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\n" +
            "Content-Length: ${bytes.size}\r\nConnection: close\r\n\r\n"
        client.getOutputStream().apply {
            write(header.toByteArray(Charsets.US_ASCII))
            write(bytes)
            flush()
        }
    }
}
