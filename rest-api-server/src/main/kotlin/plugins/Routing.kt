package org.ecgproject.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val dbHandler = DatabaseHandler()
    val apiHandler = APIHandler(dbHandler)

    routing {
        post("/") {
            apiHandler.testPostAPI(call)
        }
        post("/sensor") {
            apiHandler.saveData(call)
        }

        put("/sensor/id/{id}") {
            apiHandler.updateData(call)
        }

        authenticate("security-ecg") {
            // testing program
            get("/") {
                apiHandler.testGetAPI(call)
            }
            // program main
            get("/sensor") {
                apiHandler.getAllData(call)
            }
            get("/sensor/id/{id}") {
                apiHandler.getDataById(call)
            }
            get("/sensor/sensorid/{sensorid}"){
                apiHandler.getDataBySensorID(call)
            }
            delete("/sensor/id/{id}") {
                apiHandler.deleteDataById(call)
            }
            delete("/sensor/sensorid/{sensorid}") {
                apiHandler.deleteDataBySensorId(call)
            }
        }
    }
}
