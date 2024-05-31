package org.ecgproject.plugins


import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DatabaseHandler {
    private fun createDatabase() {
        Database.connect("jdbc:sqlite:data_ecg.db", driver = "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = TRANSACTION_SERIALIZABLE
        transaction {
            SchemaUtils.create(SensorData)
        }
    }

    private fun DateTimeServer(): String {
        // change time zone to GMT+7
        val currentTime = LocalDateTime.now()
        val gmtPlus7ZoneId = ZoneId.of("Asia/Jakarta")
        val gmtPlus7DateTime = ZonedDateTime.of(currentTime, gmtPlus7ZoneId)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy (HH.mm.ss)")
        val formatterDate = gmtPlus7DateTime.format(formatter)

        return formatterDate
    }

    fun saveData(sensorID: Int = 1, value: Double): SensorDataModel? {
        createDatabase()
        var sensorData: SensorDataModel? = null
        var idPost: Int
        val datetime = DateTimeServer()

        transaction {
            // insert new data/post data
            SensorData.insert {
                it[SensorData.sensorID] = sensorID
                it[SensorData.value] = value
                it[createDate] = datetime
            }

            // see result data if success post
            idPost = SensorData.select {
                SensorData.value eq value and (SensorData.sensorID eq sensorID)
            }.last()[SensorData.id]

            val result = SensorData.select { SensorData.id eq idPost }.singleOrNull()
            result?.let {
                sensorData = SensorDataModel.fromResultRow(result)
            }
        }
        return sensorData
    }

    fun findAllData(): List<SensorDataModel> {
        createDatabase()
        var sensorDataList = listOf<SensorDataModel>()
        transaction {
            sensorDataList = SensorData.selectAll().map {
                SensorDataModel.fromResultRow(it)
            }
        }
        return sensorDataList
    }

    fun findDataById(id: Int): SensorDataModel? {
        createDatabase()
        var sensorData: SensorDataModel? = null
        transaction {
            val result = SensorData.select { SensorData.id eq id }.singleOrNull()
            result?.let {
                sensorData = SensorDataModel.fromResultRow(result)
            }
        }
        return sensorData
    }

    fun findDataBySensorId(sensorID: Int? = null): List<SensorDataModel> {
        createDatabase()
        var sensorDataList = listOf<SensorDataModel>()
        transaction {
            sensorDataList = sensorID?.let {
                SensorData.select { SensorData.sensorID eq sensorID }.map {
                    SensorDataModel.fromResultRow(it)
                }
            } ?: SensorData.selectAll().map { SensorDataModel.fromResultRow(it) }
        }
        return sensorDataList
    }

    fun updateData(id: Int, sensorID: Int, value: Double): SensorDataModel? {
        createDatabase()
        var sensorData: SensorDataModel? = null
        val datetime = DateTimeServer()

        transaction {
            SensorData.update({ SensorData.id eq id }) {
                it[SensorData.sensorID] = sensorID
                it[SensorData.value] = value
                it[updateDate] = datetime
            }

            // see result data if success post
            val result = SensorData.select { SensorData.id eq id }.lastOrNull()
            result?.let {
                sensorData = SensorDataModel.fromResultRow(result)
            }
        }

        return sensorData
    }

    fun deleteDataById(id: Int) {
        createDatabase()
        transaction {
            SensorData.deleteWhere { SensorData.id eq id }
        }
    }

    fun deleteDataBysensorId(sensorID: Int) {
        createDatabase()
        transaction {
            SensorData.deleteWhere { SensorData.sensorID eq sensorID }
        }
    }
}

object SensorData : Table() {
    val id = this.integer("id").autoIncrement()
    val sensorID = this.integer("sensorid").default(1)
    val value = this.double("datasensor")
    val createDate = this.text("createdate").default("").nullable()
    val updateDate = this.text("updatedate").default("").nullable()

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class SensorDataModel(val id: Int, val sensorid: Int, val value: Double, val createDate: String?, val updateDate: String?) {
    companion object {
        fun fromResultRow(row: ResultRow): SensorDataModel {
            return SensorDataModel (
                id = row[SensorData.id],
                sensorid = row[SensorData.sensorID],
                value = row[SensorData.value],
                createDate = row[SensorData.createDate],
                updateDate = row[SensorData.updateDate]
            )
        }
    }
}
