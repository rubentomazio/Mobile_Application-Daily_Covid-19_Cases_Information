package pt.ulusofona.deisi.a2020.cm.g7.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.countie_view_list.view.*
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.Countie

class CountieAdapter(private var dataSource: List<Countie>) : RecyclerView.Adapter<CountieAdapter.CountieViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.countie_view_list, parent, false)
        return CountieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CountieViewHolder, position: Int) {
        val currentItem = dataSource[position]

        holder.countie.text = currentItem.countie
        holder.district.text = currentItem.district
        holder.lastUpdate.text = "${holder.itemView.getContext().getString(R.string.last_update_output)}" + " " + currentItem.date
        holder.incidente?.text = "${holder.itemView.getContext().getString(R.string.incidence_output)}" + " " + currentItem.incidencia.toString()
        holder.degreeIncidente.text = "${holder.itemView.getContext().getString(R.string.degree_of_incidence_output)}" + " " + currentItem.incidenciaRisco

        //landscape
        holder.ars?.text = "ARS: " + currentItem.ars
        holder.population?.text = "${holder.itemView.getContext().getString(R.string.population_output)}" + " " + currentItem.population
        holder.cases?.text = "${holder.itemView.getContext().getString(R.string.cases_output)}" + " " + currentItem.cases
    }

    override fun getItemCount() = dataSource.size

    class CountieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countie: TextView = itemView.counties
        val district: TextView = itemView.district
        val lastUpdate: TextView = itemView.last_update
        val incidente: TextView? = itemView.incidence
        val degreeIncidente: TextView = itemView.degree_incidence

        //landscape
        val ars: TextView? = itemView.ars2
        val population: TextView? = itemView.population
        val cases: TextView? = itemView.cases
    }
}
