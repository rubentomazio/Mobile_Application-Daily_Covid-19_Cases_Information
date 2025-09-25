package pt.ulusofona.deisi.a2020.cm.g7.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.Converters
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "covidTests")
@TypeConverters(Converters::class)
@Parcelize
class CovidTest(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    var result: String,
    var location: String,
    var dateTest: Date = Date()
) : Parcelable {

    fun setDate(date: Date) {
        dateTest = date
    }

    fun getDate(): String {

        val sdf = SimpleDateFormat("dd-MM-yyyy")

        return sdf.format(dateTest)
    }
}