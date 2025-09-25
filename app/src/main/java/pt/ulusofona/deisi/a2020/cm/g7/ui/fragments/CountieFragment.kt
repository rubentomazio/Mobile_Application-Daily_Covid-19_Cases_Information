package pt.ulusofona.deisi.a2020.cm.g7.ui.fragments

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_counties.*
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.Countie
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.AppDatabase
import pt.ulusofona.deisi.a2020.cm.g7.ui.activities.countiesList
import pt.ulusofona.deisi.a2020.cm.g7.ui.adapters.CountieAdapter

class CountieFragment : Fragment() {

    lateinit var adapter: CountieAdapter
    var dialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(getConnectionStatusNow() == false) {
            Toast.makeText(activity as Context, "${getString(R.string.no_connection)}", Toast.LENGTH_LONG).show()
        }
        val view = inflater.inflate(R.layout.fragment_counties, container, false)
        dialog = Dialog(activity as Context)
        return view
    }

    override fun onResume() {
        filter_fragment.visibility = INVISIBLE
        super.onResume()
    }

    override fun onStart() {
        countiesList = AppDatabase.getDatabase(activity as Context).countieDAO().getAll()
        buildRecyclerView(countiesList)

        super.onStart()

        button_filter.setOnClickListener {
            filter_fragment.visibility = VISIBLE
            checkbox_0_120.isChecked = context?.getSharedPreferences("filter", 0)!!.getBoolean("0-120", false)
            checkbox_120_240.isChecked=context?.getSharedPreferences("filter", 0)!!.getBoolean("120-240", false)
            checkbox_240_480.isChecked=context?.getSharedPreferences("filter", 0)!!.getBoolean("240-480", false)
            checkbox_480_960.isChecked = context?.getSharedPreferences("filter", 0)!!.getBoolean("480-960", false)
            checkbox_960.isChecked=context?.getSharedPreferences("filter", 0)!!.getBoolean("960+", false)
            checkbox_low.isChecked = context?.getSharedPreferences("filter", 0)!!.getBoolean("0-120", false)
            checkbox_moderate.isChecked=context?.getSharedPreferences("filter", 0)!!.getBoolean("120-240", false)
            checkbox_high.isChecked=context?.getSharedPreferences("filter", 0)!!.getBoolean("240-480", false)
            checkbox_very_high.isChecked = context?.getSharedPreferences("filter", 0)!!.getBoolean("480-960", false)
            checkbox_extremely_high.isChecked=context?.getSharedPreferences("filter", 0)!!.getBoolean("960+", false)
        }

        button_close.setOnClickListener {
            filter_fragment.visibility = INVISIBLE
        }

        button_ok.setOnClickListener {
            var filterList = listOf<Countie>()

            val filterIncidence = getCheckbox()
            var isFilterIncidence = false
            val incidenceList = getFilterIncidence(filterIncidence)
            val minIncidence = incidenceList[0]
            val maxIncidence = incidenceList[1]

            var isFilterIncidenceDegree = false
            val incidenceDegreeList = getFilterIncidenceDegree()

            if (getCheckbox() == 1 || getCheckbox() == 2 || getCheckbox() == 3 || getCheckbox() == 4 || getCheckbox() == 5) {
                isFilterIncidence = true
            } else if (getCheckbox() == 6) {
                isFilterIncidence = false
            }

            if (getCheckbox2() == 1 || getCheckbox2() == 2 || getCheckbox2() == 3 || getCheckbox2() == 4 || getCheckbox2() == 5) {
                isFilterIncidenceDegree = true
            } else if (getCheckbox2() == 6) {
                isFilterIncidenceDegree = false
            }

            // Filtros ativos
            if (isFilterIncidence && !isFilterIncidenceDegree) {
                filterList = AppDatabase.getDatabase(activity as Context).countieDAO()
                    .getIncidence(minIncidence, maxIncidence)
            } else if (isFilterIncidenceDegree && !isFilterIncidence) {
                filterList = AppDatabase.getDatabase(activity as Context).countieDAO()
                    .getDegreeIncidence(incidenceDegreeList)
            } else if (isFilterIncidence && isFilterIncidenceDegree) {
                filterList = AppDatabase.getDatabase(activity as Context).countieDAO()
                    .getIncidenceAndDegreeIncidence(minIncidence, maxIncidence, incidenceDegreeList)
            } else {
                filterList = AppDatabase.getDatabase(activity as Context).countieDAO().getAll()
            }

            buildRecyclerView(filterList)
            filter_fragment.visibility = INVISIBLE
        }
    }

    fun getFilterIncidence(filterIncidence: Int): List<Int> {
        var min = 0
        var max = 0
        val filterList = mutableListOf<Int>()

        when (filterIncidence) {
            1 -> {
                context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("0-120", true)
                min = 0
                max = 120
            }
            2 -> {
                context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("120-240", true)
                min = 120
                max = 240
            }
            3 -> {
                context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("240-480", true)
                min = 240
                max = 480
            }
            4 -> {
                context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("480-960", true)
                min = 480
                max = 960
            }
            5 -> {
                context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("960+", true)
                min = 960
                max = 2000
            }
        }

        filterList.add(min)
        filterList.add(max)

        return filterList
    }

    fun getFilterIncidenceDegree(): String {
        if (checkbox_low.isChecked) {
            context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("0-120", true)
            checkbox_moderate.isChecked = false
            checkbox_high.isChecked = false
            checkbox_very_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return "Baixo a Moderado"
        } else if (checkbox_moderate.isChecked) {
            context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("120-240", true)
            checkbox_low.isChecked = false
            checkbox_high.isChecked = false
            checkbox_very_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return "Moderado"
        } else if (checkbox_high.isChecked) {
            context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("240-480", true)
            checkbox_low.isChecked = false
            checkbox_moderate.isChecked = false
            checkbox_very_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return "Elevado"
        } else if (checkbox_very_high.isChecked) {
            context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("480-960", true)
            checkbox_low.isChecked = false
            checkbox_moderate.isChecked = false
            checkbox_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return "Muito Elevado"
        } else if (checkbox_extremely_high.isChecked) {
            context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("960+", true)
            checkbox_low.isChecked = false
            checkbox_moderate.isChecked = false
            checkbox_high.isChecked = false
            checkbox_very_high.isChecked = false
            return "Extremamente Elevado"
        } else {
            checkbox_low.isChecked = false
            checkbox_moderate.isChecked = false
            checkbox_high.isChecked = false
            checkbox_very_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return ""
        }
    }

    fun getCheckbox(): Int {
        if (checkbox_0_120.isChecked) {
            checkbox_120_240.isChecked = false
            checkbox_240_480.isChecked = false
            checkbox_480_960.isChecked = false
            checkbox_960.isChecked = false
            return 1
        } else if (checkbox_120_240.isChecked) {
            checkbox_0_120.isChecked = false
            checkbox_240_480.isChecked = false
            checkbox_480_960.isChecked = false
            checkbox_960.isChecked = false
            return 2
        } else if (checkbox_240_480.isChecked) {
            checkbox_0_120.isChecked = false
            checkbox_120_240.isChecked = false
            checkbox_480_960.isChecked = false
            checkbox_960.isChecked = false
            return 3
        } else if (checkbox_480_960.isChecked) {
            checkbox_0_120.isChecked = false
            checkbox_120_240.isChecked = false
            checkbox_240_480.isChecked = false
            checkbox_960.isChecked = false
            return 4
        } else if (checkbox_960.isChecked) {
            checkbox_0_120.isChecked = false
            checkbox_120_240.isChecked = false
            checkbox_480_960.isChecked = false
            checkbox_240_480.isChecked = false
            return 5
        } else {
            checkbox_0_120.isChecked = false
            checkbox_120_240.isChecked = false
            checkbox_240_480.isChecked = false
            checkbox_480_960.isChecked = false
            checkbox_960.isChecked = false
            return 6
        }
    }

    fun getCheckbox2(): Int {
        if (checkbox_low.isChecked) {
            checkbox_moderate.isChecked = false
            checkbox_high.isChecked = false
            checkbox_very_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return 1
        } else if (checkbox_moderate.isChecked) {
            checkbox_low.isChecked = false
            checkbox_high.isChecked = false
            checkbox_very_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return 2
        } else if (checkbox_high.isChecked) {
            checkbox_low.isChecked = false
            checkbox_moderate.isChecked = false
            checkbox_very_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return 3
        } else if (checkbox_very_high.isChecked) {
            checkbox_low.isChecked = false
            checkbox_moderate.isChecked = false
            checkbox_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return 4
        } else if (checkbox_extremely_high.isChecked) {
            checkbox_low.isChecked = false
            checkbox_moderate.isChecked = false
            checkbox_very_high.isChecked = false
            checkbox_high.isChecked = false
            return 5
        } else {
            checkbox_low.isChecked = false
            checkbox_moderate.isChecked = false
            checkbox_high.isChecked = false
            checkbox_very_high.isChecked = false
            checkbox_extremely_high.isChecked = false
            return 6
        }
    }

    fun buildRecyclerView(counties: List<Countie>) {
        adapter = CountieAdapter(counties)

        recycler_view?.adapter = adapter
        recycler_view?.layoutManager = LinearLayoutManager(activity)
        recycler_view?.setHasFixedSize(true)
    }

    private fun getConnectionStatusNow(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}