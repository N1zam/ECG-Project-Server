package org.ecgproject.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

class APIHandler(private val dbHandler: DatabaseHandler) {
    suspend fun testGetAPI(call: ApplicationCall) {
        val param = DataAPITest(key = "Hello, World")
        call.respond(HttpStatusCode.OK, param)
    }

    suspend fun testPostAPI(call: ApplicationCall) {
        val param = call.receive<DataAPITest>()
        val key = param.key
        val name = param.name

        val result = DataAPITest(key, name)
        call.respond(HttpStatusCode.OK, result)
    }

    suspend fun saveData(call: ApplicationCall) {
        try {
            val param = call.receive<SensorDataAPI>()
            val result = dbHandler.saveData(
                sensorID = param.sensorid,
                value = param.value
            )

            if (result == null) {
                call.respondText("Data Sensor tidak berhasil disimpan", status = HttpStatusCode.BadRequest)
                return
            }

            call.respond(HttpStatusCode.OK, result)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }

    suspend fun updateData(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID tidak valid")
                return
            }

            val sensorData = call.receive<SensorDataAPI>()
            var result = dbHandler.updateData(id, sensorData.sensorid, sensorData.value)

            if(result != null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                result = dbHandler.saveData(id, sensorData.value)
                result?.let {
                    call.respond(HttpStatusCode.OK, result)
                } ?: return
            }

        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError,"Error: ${e.message}")
        }
    }

    suspend fun getAllData(call: ApplicationCall) {
        try {
            val sensorDataList = dbHandler.findAllData()
            if(sensorDataList.isEmpty()) {
                call.respondText("Tidak ada data", status = HttpStatusCode.NotFound)
                return
            }
            call.respond(HttpStatusCode.OK, sensorDataList)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }

    suspend fun getDataById(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            if(id != null) {
                val sensorData = dbHandler.findDataById(id)
                if (sensorData == null) {
                    call.respondText("Data sensor dengan ID $id tidak ditemukan", status = HttpStatusCode.NotFound)
                    return
                }
                call.respond(HttpStatusCode.OK, sensorData)

            } else {
                call.respondText("Parameter ID tidak valid", status = HttpStatusCode.BadRequest)
            }

        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }

    suspend fun getDataBySensorId(call: ApplicationCall) {
        try {
            val sensorId = call.parameters["sensorid"]?.toIntOrNull()
            sensorId?.let {
                val sensorData = dbHandler.findDataBySensorId(sensorId)
                call.respond(HttpStatusCode.OK, sensorData)
            } ?: call.respondText("Parameter Sensor ID tidak valid", status = HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }

    suspend fun deleteDataById(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            id?.let {
                dbHandler.deleteDataById(id)
                call.respondText("Data sensor dengan ID $id berhasil dihapus")

            } ?: call.respondText("Parameter ID tidak valid", status = HttpStatusCode.BadRequest)
        }
        catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error:${e.message}")
        }
    }

    suspend fun deleteDataBySensorId(call: ApplicationCall) {
        try {
            val sensorId = call.parameters["sensorid"]?.toIntOrNull()
            sensorId?.let {
                dbHandler.deleteDataBysensorId(sensorId)
                call.respondText("Data sensor dengan sensor id $sensorId telah dihapus")
            } ?: call.respond(HttpStatusCode.BadRequest, "Parameter Sensor ID tidak valid")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }
}

@Serializable
data class DataAPITest(val key: String = "", val name: String = "Kotlin")

@Serializable
data class SensorDataAPI(val id: Int = 0, val sensorid: Int, val value: Double)
