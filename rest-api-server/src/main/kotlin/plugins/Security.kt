package org.ecgproject.plugins

import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.auth.*
import io.ktor.server.application.*

class Security(private val app: Application) {
    fun AuthenticationServer(nameAuth: String) {
        var user = System.getenv("USER")
        var password = System.getenv("PASSWORD")
        if(user == null || password == null) {
            val dotenv = Dotenv.configure().load()
            user = dotenv["USER"]
            password = dotenv["PASSWORD"]
        }
        app.authentication {
            basic(name = nameAuth) {
                realm = "Ktor Server"
                validate { credentials ->
                    if(credentials.name == user && credentials.password == password) {
                        UserIdPrincipal(credentials.name)
                    }
                    else {
                        null
                    }
                }
            }
        }
    }
}
