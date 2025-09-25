package pt.ulusofona.deisi.a2020.cm.g7.domain.covidTests

import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.dao.CovidTestsDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.CovidTest
import pt.ulusofona.deisi.a2020.cm.g7.ui.listeners.OnDataReceivedListener

class CovidTestsLogic(private val storage: CovidTestsDAO) {

    // Observer é notificado com um ArrayList <CovidTest>
    private var listener: OnDataReceivedListener? = null

    // Insere um novo teste na base de dados e faz read()
    fun create(covidTest: CovidTest) {
        CoroutineScope(Dispatchers.IO).launch {
            storage.insert(covidTest)
            read()
        }
    }

    // Lê os testes da base de dados e notifica o observer
    fun read() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = storage.getAll()
            notifyDataChanged(ArrayList(list))
        }
    }

    // Notifica o observer com UI Thread context
    private suspend fun notifyDataChanged(list: ArrayList<CovidTest>) {
        withContext(Dispatchers.Main) {
            listener?.onDataReceived(list)
        }
    }

    // Regista o observer como listener
    fun registerListener(listener: OnDataReceivedListener) {
        this.listener = listener
    }

    // Apaga a referência do observer do listener
    fun unregisterListener() {
        this.listener = null
    }
}