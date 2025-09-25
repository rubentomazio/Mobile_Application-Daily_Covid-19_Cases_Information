package pt.ulusofona.deisi.a2020.cm.g7.data.local.room

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(dateTest: Date?): Long? {
        return dateTest?.time?.toLong()
    }
}