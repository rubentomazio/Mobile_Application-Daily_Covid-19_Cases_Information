package pt.ulusofona.deisi.a2020.cm.g7.data.local.room.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.Countie

@Dao
interface CountieDAO {

    @Insert(onConflict = REPLACE)
    fun insertAll(counties: List<Countie>)

    @Query("SELECT * FROM counties")
    fun getAll(): List<Countie>

    //Query para os filtros

    @Query("SELECT * FROM counties WHERE incidenciaRisco=:incidenciaRisco ")
    fun getDegreeIncidence(incidenciaRisco: String): List<Countie>

    @Query("SELECT * FROM counties WHERE incidencia>:minimo AND incidencia<:maximo")
    fun getIncidence(minimo: Int, maximo: Int): List<Countie>

    @Query("SELECT * FROM counties WHERE incidencia>:minimo AND incidencia<:maximo OR incidenciaRisco=:incidenciaRisco")
    fun getIncidenceAndDegreeIncidence(minimo: Int, maximo: Int, incidenciaRisco: String): List<Countie>
}
