package pt.ulusofona.deisi.a2020.cm.g7.data.local.room

import android.content.Context
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.Countie

class CountieRepository(context: Context) {

    private val dataBase = AppDatabase.getDatabase(context).countieDAO()

    fun insertAll(counties: List<Countie>) {
        return dataBase.insertAll(counties)
    }

    fun getAll(): List<Countie> {
        return dataBase.getAll()
    }

    fun registerListener(){
    }
}