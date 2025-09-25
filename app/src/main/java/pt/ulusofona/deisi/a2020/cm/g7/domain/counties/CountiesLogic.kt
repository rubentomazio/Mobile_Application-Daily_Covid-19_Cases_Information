package pt.ulusofona.deisi.a2020.cm.g7.domain.counties

import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.CountieRepository

class CountiesLogic(private val repository: CountieRepository) {

    fun registerListener(){
        repository.registerListener()
    }
}