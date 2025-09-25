package pt.ulusofona.deisi.a2020.cm.g7.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.CovidTest
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.AppDatabase
import pt.ulusofona.deisi.a2020.cm.g7.domain.covidTests.CovidTestsLogic
import pt.ulusofona.deisi.a2020.cm.g7.ui.listeners.OnDataReceivedListener

class CovidTestsListViewModel(application: Application) : AndroidViewModel(application), OnDataReceivedListener {

    private val localDatabase = AppDatabase.getDatabase(application).covidTestsDAO()
    private val logic = CovidTestsLogic(localDatabase)

    // O listener é notificado com o ArrayList<CovidTests>
    private var listener: OnDataReceivedListener? = null
    var covidTests = ArrayList<CovidTest>()

    fun read() {
        this.logic.read()
    }

    fun insert(covidTest: CovidTest) {
        logic.create(covidTest)
    }

    fun registerListener(listener: OnDataReceivedListener) {
        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {
        this.listener = null
        this.logic.unregisterListener()
    }

    private fun onDataChanged() {
        this.listener?.onDataReceived(this.covidTests)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {
        data?.let { this.covidTests = it as ArrayList<CovidTest> }
        onDataChanged()
    }
}