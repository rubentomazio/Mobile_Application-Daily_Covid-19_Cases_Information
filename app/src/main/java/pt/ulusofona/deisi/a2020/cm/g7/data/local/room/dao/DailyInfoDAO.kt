package pt.ulusofona.deisi.a2020.cm.g7.data.local.room.dao

import androidx.room.*
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.DailyInfo

@Dao
interface DailyInfoDAO {

    @Query("DELETE FROM daily_info")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dailyInfo: DailyInfo)

    @Query("SELECT * FROM daily_info")
    fun getAll(): DailyInfo
}