package pt.ulusofona.deisi.a2020.cm.g7.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_countie_search.*
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.Countie
import pt.ulusofona.deisi.a2020.cm.g7.ui.activities.countieClicked

class CountieSearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_countie_search, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showCountie(countieClicked)
    }

    override fun onStart() {
        super.onStart()
    }

    fun showCountie(countie: Countie) {
        countie.let {
            counties.text = countie.countie
            district.text = countie.district
            last_update.text = "${getString(R.string.last_update_output)}" +  " " + countie.date
            incidence.text = "${getString(R.string.incidence_output)}" +  " " + countie.incidencia.toString()
            degree_incidence.text = "${getString(R.string.degree_of_incidence_output)}" +  " " + countie.incidenciaRisco
        }
    }
}