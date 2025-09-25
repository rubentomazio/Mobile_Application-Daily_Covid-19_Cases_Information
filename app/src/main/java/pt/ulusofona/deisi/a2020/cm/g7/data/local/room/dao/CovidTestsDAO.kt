package pt.ulusofona.deisi.a2020.cm.g7.data.local.room.dao

import androidx.room.*
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.CovidTest

@Dao
interface CovidTestsDAO {

    @Insert
    fun insert(covidTest: CovidTest)

    @Query("SELECT * FROM covidTests")
    fun getAll(): List<CovidTest>

    //Query para os filtros

    @Query("SELECT * FROM covidTests WHERE result=:result ")
    fun getResult(result: String): List<CovidTest>

    @Query("SELECT * FROM covidTests ORDER BY dateTest ASC")
    fun orderByDateASC(): List<CovidTest>

    @Query("SELECT * FROM covidTests ORDER BY dateTest DESC")
    fun orderByDateDESC(): List<CovidTest>

    @Query("SELECT * FROM covidTests  WHERE result=:result ORDER BY dateTest ASC")
    fun getResultByDateASC(result: String): List<CovidTest>

    @Query("SELECT * FROM covidTests  WHERE result=:result ORDER BY dateTest DESC")
    fun getResultByDateDESC(result: String): List<CovidTest>
}