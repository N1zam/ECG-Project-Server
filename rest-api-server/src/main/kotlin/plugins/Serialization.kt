package org.ecgproject.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

class Serialization(app: Application) {
    init {
        app.install(ContentNegotiation) {
            json()
        }
    }
}
