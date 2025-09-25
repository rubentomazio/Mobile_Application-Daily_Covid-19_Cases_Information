package pt.ulusofona.deisi.a2020.cm.g7.data.local.list

import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.CovidTest
import kotlin.collections.ArrayList

class Storage {

    private val covidTests = ArrayList<CovidTest>()

    companion object {

        private var instance: Storage? = null

        fun getInstance(): Storage {
            synchronized(this) {
                if (instance == null) {
                    instance =
                        Storage()
                }
                return instance as Storage
            }
        }
    }

    fun getCovidTest(): ArrayList<CovidTest> {
        return this.covidTests
    }
}