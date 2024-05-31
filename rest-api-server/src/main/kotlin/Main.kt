package org.ecgproject

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class MainServer {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val serverThread1 = Thread(
                ServerRunnable(
                    "Server Rest API 1",
                    "127.0.0.1",
                    8080,
                    Application::moduleServer1
                )
            )

            val serverThread2 = Thread(
                ServerRunnable(
                    "Server Rest API 2",
                    "127.0.0.1",
                    3000,
                    Application::moduleServer2
                )
            )

            // Running Thread
            serverThread1.start()
            serverThread2.start()
        }
    }
}

private class ServerRunnable(
    private val name: String,
    private val host: String,
    private val port: Int,
    private val moduleFunction: Application.() -> Unit
): Runnable {
    override fun run() {
        println("$name: RUNNING...")
        embeddedServer(Netty, port = port, host = host, module = moduleFunction)
            .start(wait = true)
    }
}
