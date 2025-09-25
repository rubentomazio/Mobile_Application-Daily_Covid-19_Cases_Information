package pt.ulusofona.deisi.a2020.cm.g7.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaccine_info")
class VaccineInfo(
    @PrimaryKey
    val date2: String,
    val totalVaccineDoses: String,
    val totalFirstVaccineDoses: String,
    val dailyFirstVaccineDoses: String,
    val percentageFirstVaccineDoses: String,
    val totalSecondVaccineDoses: String,
    val dailySecondVaccineDoses: String,
    val percentageSecondVaccineDoses: String
)