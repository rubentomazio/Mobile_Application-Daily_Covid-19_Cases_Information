package pt.ulusofona.deisi.a2020.cm.g7.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.VaccineInfo

@Dao
interface VaccineInfoDAO {

    @Query("DELETE FROM vaccine_info")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vaccineInfo: VaccineInfo)

    @Query("SELECT * FROM vaccine_info")
    fun getAll(): VaccineInfo
}