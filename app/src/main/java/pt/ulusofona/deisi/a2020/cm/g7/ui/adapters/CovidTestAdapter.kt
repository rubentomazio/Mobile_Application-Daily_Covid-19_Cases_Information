package pt.ulusofona.deisi.a2020.cm.g7.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.covid_tests_list_item.view.*
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.CovidTest
import pt.ulusofona.deisi.a2020.cm.g7.ui.listeners.OnTouchListener

class CovidTestAdapter(private val listener: OnTouchListener, private val context: Context, private val layout: Int, private val items: MutableList<CovidTest>) :
    RecyclerView.Adapter<CovidTestAdapter.CovidTestsViewHolder>() {

    class CovidTestsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val result: TextView = view.result
        val location: TextView = view.location
        val date: TextView = view.date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CovidTestsViewHolder {
        return CovidTestsViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CovidTestsViewHolder, position: Int) {

        val result =  items[position].result
        val location =  items[position].location
        val date =  items[position].getDate()

        holder.result.text = result
        holder.location.text = location
        holder.date.text = date

        holder.itemView.setOnClickListener { listener.onClickEvent(items[position]) }
    }
}