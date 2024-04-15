package org.ecgproject.plugins

import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.auth.*
import io.ktor.server.application.*

fun Application.configureSecurity() {
    var user = System.getenv("USER")
    var password = System.getenv("PASSWORD")
    if(user == null || password == null) {
        val dotenv = Dotenv.load()
        user = dotenv["USER"]
        password = dotenv["PASSWORD"]
    }

    authentication {
        basic(name="security-ecg") {
            realm = "Ktor Server"
            validate { credentials ->
                if(credentials.name == user && credentials.password == password)
                {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}