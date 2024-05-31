package org.ecgproject

import io.ktor.server.application.*
import org.ecgproject.plugins.Routing
import org.ecgproject.plugins.Security
import org.ecgproject.plugins.Serialization

fun Application.moduleServer1() {
    Serialization(this)
    Security(this).AuthenticationServer("security-ecg")
    Routing(this).configureRoutingWithAuth("security-ecg")
}

fun Application.moduleServer2() {
    Serialization(this)
    Routing(this).configureRoutingWithoutAuth()
}
