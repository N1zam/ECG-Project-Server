package org.ecgproject.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

val dbHandler = DatabaseHandler()
val apiHandler = APIHandler(dbHandler)

class Routing(private val app: Application) {
    fun configureRoutingWithAuth(nameAuth: String) {
        app.routing {
            authenticate(nameAuth) {
                // testing program
                get("/") {
                    apiHandler.testGetAPI(call)
                }

                // program main
                // method API : get
                get("/sensor") {
                    apiHandler.getAllData(call)
                }
                get("/sensor/id/{id}") {
                    apiHandler.getDataById(call)
                }
                get("/sensor/sensorid/{sensorid}") {
                    apiHandler.getDataBySensorId(call)
                }
                // method API : delete
                delete("/sensor/id/{id}") {
                    apiHandler.deleteDataById(call)
                }
                delete("/sensor/sensorid/{sensorid}") {
                    apiHandler.deleteDataBySensorId(call)
                }
            }
        }
    }

    fun configureRoutingWithoutAuth() {
        app.routing {
            // method API : post
            post("/") {
                apiHandler.testPostAPI(call)
            }
            post("/sensor") {
                apiHandler.saveData(call)
            }

            // method API : put
            put("/sensor/id/{id}") {
                apiHandler.updateData(call)
            }
        }
    }
}
