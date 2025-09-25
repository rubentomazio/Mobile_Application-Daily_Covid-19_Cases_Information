package pt.ulusofona.deisi.a2020.cm.g7.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "daily_info")
class DailyInfo(
    @PrimaryKey
    @SerializedName("data") var date: String,
    @SerializedName("confirmados_novos") var newConfirmed: Int = 0,
    @SerializedName("ativos") var active: Int = 0,
    @SerializedName("confirmados") var confirmed: Int = 0,
    @SerializedName("recuperados") var recovered: Int = 0,
    @SerializedName("internados") var hospitalized: Int = 0,
    @SerializedName("internados_uci") var UCIhospitalized: Int = 0,
    @SerializedName("obitos") var totalDeaths: Int = 0,
    @SerializedName("rt_nacional") var rt: Double = 0.00
)