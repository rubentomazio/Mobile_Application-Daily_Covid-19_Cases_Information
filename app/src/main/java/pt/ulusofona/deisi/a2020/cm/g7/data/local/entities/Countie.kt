package pt.ulusofona.deisi.a2020.cm.g7.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "counties")
class Countie(
    @PrimaryKey
    @SerializedName("concelho") var countie: String,
    @SerializedName("distrito") var district: String,
    @SerializedName("data") var date: String,
    @SerializedName("incidencia_risco") val incidenciaRisco: String,
    var incidencia: Int,
    var ars: String,
    val population: Int,
    @SerializedName("casos_14dias") val cases: Int
)