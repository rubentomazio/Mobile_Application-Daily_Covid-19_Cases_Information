package pt.ulusofona.deisi.a2020.cm.g7.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.CountieRepository
import pt.ulusofona.deisi.a2020.cm.g7.domain.counties.CountiesLogic

class CountieViewModel (application: Application) : AndroidViewModel(application) {

    private val countieLogic =
        CountiesLogic(repository = CountieRepository(getApplication()))

    fun registerListener(){
        countieLogic.registerListener()
    }
}