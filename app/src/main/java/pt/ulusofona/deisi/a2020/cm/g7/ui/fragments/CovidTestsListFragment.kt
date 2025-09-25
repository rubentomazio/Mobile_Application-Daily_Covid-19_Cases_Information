package pt.ulusofona.deisi.a2020.cm.g7.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ulusofona.deisi.a2020.cm.g7.ui.adapters.CovidTestAdapter
import pt.ulusofona.deisi.a2020.cm.g7.ui.viewmodels.CovidTestsListViewModel
import kotlinx.android.synthetic.main.fragment_covid_tests_list.*
import kotlinx.android.synthetic.main.fragment_covid_tests_list.view.*
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.CovidTest
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.AppDatabase
import pt.ulusofona.deisi.a2020.cm.g7.ui.listeners.OnDataReceivedListener
import pt.ulusofona.deisi.a2020.cm.g7.ui.listeners.OnNavigationListener
import pt.ulusofona.deisi.a2020.cm.g7.ui.listeners.OnTouchListener

const val EXTRA_COVID_TEST = "TEST"

class CovidTestsListFragment : Fragment(), OnDataReceivedListener, OnTouchListener {

    private lateinit var viewModel: CovidTestsListViewModel

    private var listener: OnNavigationListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_covid_tests_list, container, false)
        view.covid_tests.layoutManager = LinearLayoutManager(activity as Context)

        view.fab.setOnClickListener {
            this.listener?.onNavigateToCovidTestForm(null)
        }

        viewModel = ViewModelProviders.of(this).get(CovidTestsListViewModel::class.java)

        return view
    }

    override fun onResume() {
        covid_tests_filter_fragment.visibility = View.INVISIBLE
        super.onResume()
    }

    override fun onStart() {
        this.viewModel.covidTests.let { onDataChanged(it) }
        this.viewModel.registerListener(this)
        this.listener = activity as OnNavigationListener
        this.viewModel.read()

        button_covid_tests_filter.setOnClickListener {
            covid_tests_filter_fragment.visibility = View.VISIBLE
            checkbox_positive.isChecked = context?.getSharedPreferences("filter", 0)!!.getBoolean("positive", false)
            checkbox_negative.isChecked=context?.getSharedPreferences("filter", 0)!!.getBoolean("negative", false)
            checkbox_ascending.isChecked=context?.getSharedPreferences("filter", 0)!!.getBoolean("ascending", false)
            checkbox_descending.isChecked = context?.getSharedPreferences("filter", 0)!!.getBoolean("descending", false)
        }

        button_close2.setOnClickListener {
            covid_tests_filter_fragment.visibility = View.INVISIBLE
        }

        button_ok2.setOnClickListener {
            var filterList = ArrayList<CovidTest>()

            val filterResult = getResult()
            var isFilterResult = false

            if (!getResult().equals("")) {
                isFilterResult = true
            } else if (getResult().equals("")) {
                isFilterResult = false
            }

            if (isFilterResult) {
                when {
                    checkbox_ascending.isChecked -> {
                        filterList = AppDatabase.getDatabase(activity as Context).covidTestsDAO()
                            .getResultByDateASC(filterResult) as ArrayList<CovidTest>
                    }
                    checkbox_descending.isChecked -> {
                        filterList = AppDatabase.getDatabase(activity as Context).covidTestsDAO()
                            .getResultByDateDESC(filterResult) as ArrayList<CovidTest>
                    }
                    else -> {
                        filterList = AppDatabase.getDatabase(activity as Context).covidTestsDAO()
                            .getResult(filterResult) as ArrayList<CovidTest>
                    }
                }
            } else {
                when {
                    checkbox_ascending.isChecked -> {
                        filterList = AppDatabase.getDatabase(activity as Context).covidTestsDAO()
                            .orderByDateASC() as ArrayList<CovidTest>
                    }
                    checkbox_descending.isChecked -> {
                        filterList = AppDatabase.getDatabase(activity as Context).covidTestsDAO()
                            .orderByDateDESC() as ArrayList<CovidTest>
                    }
                    else -> {
                        filterList = AppDatabase.getDatabase(activity as Context).covidTestsDAO()
                            .getAll() as ArrayList<CovidTest>
                    }
                }
            }

            onDataChanged(filterList)
            covid_tests_filter_fragment.visibility = View.INVISIBLE
        }

        super.onStart()
    }

    override fun onStop() {
        this.viewModel.unregisterListener()
        this.listener = null
        super.onStop()
    }

    private fun onDataChanged(data: ArrayList<CovidTest>) {

        if(data.size > 0) {
            empty_list_text.visibility = View.GONE
        } else empty_list_text.visibility = View.VISIBLE

        covid_tests.adapter =
            CovidTestAdapter(
                this,
                activity as Context,
                R.layout.covid_tests_list_item,
                data
            )
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {
        data?.let { onDataChanged(it as ArrayList<CovidTest>) }
    }

    override fun onClickEvent(data: Any?) {

        data?.let {
            val args = Bundle()
            args.putParcelable(EXTRA_COVID_TEST, data as CovidTest)
            this.listener?.onNavigateToCovidTestForm(args)
        }
    }

    fun getResult(): String {
        if (checkbox_positive.isChecked) {
            context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("positive", true)
            checkbox_negative.isChecked = false
            return "Positivo"
        } else if (checkbox_negative.isChecked) {
            context?.getSharedPreferences("filter", 0)!!.edit().putBoolean("negative", true)
            checkbox_positive.isChecked = false
            return "Negativo"
        } else {
            checkbox_negative.isChecked = false
            checkbox_positive.isChecked = false
            return ""
        }
    }
}
