package org.ecgproject

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.ecgproject.plugins.*

fun main() {
    val serverThread1 = Thread(ServerRunnable1("ServerRunnable1"))
    val serverThread2 = Thread(ServerRunnable2("ServerRunnable2"))
    serverThread1.start()
    serverThread2.start()
}

class ServerRunnable1(private val name: String): Runnable {
    override fun run() {
        embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module1)
            .start(wait = true)
    }
}

class ServerRunnable2(private val name: String): Runnable {
    override fun run() {
        embeddedServer(Netty, port = 3000, host = "127.0.0.1", module = Application::module2)
            .start(wait = true)
    }
}

fun Application.module1() {
    configureSerialization()
    configureSecurity()
    configureRoutingGet()
}

fun Application.module2() {
    configureSerialization()
    configureRoutingPost()
}