package pt.ulusofona.deisi.a2020.cm.g7.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.DailyInfo
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.AppDatabase
import pt.ulusofona.deisi.a2020.cm.g7.data.remote.services.DailyInfoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var BaseUrl = "https://covid19-api.vost.pt/"

class DailyInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(getConnectionStatusNow() == false) {
            Toast.makeText(activity as Context, "${getString(R.string.no_connection)}", Toast.LENGTH_LONG).show()
        }

        val view = inflater.inflate(R.layout.fragment_daily_info, container, false)

        val date = view.findViewById<View>(R.id.date) as TextView
        val new_confirmed = view.findViewById<View>(R.id.new_confirmed) as TextView
        val active = view.findViewById<View>(R.id.active) as TextView
        val confirmed = view.findViewById<View>(R.id.confirmed) as TextView
        val recovered = view.findViewById<View>(R.id.recovered) as TextView
        val hospitalized = view.findViewById<View>(R.id.hospitalized) as TextView
        val uci_hospitalized = view.findViewById<View>(R.id.uci_hospitalized) as TextView
        val total_deaths = view.findViewById<View>(R.id.total_deaths) as TextView
        val rt = view.findViewById<View>(R.id.rt) as TextView

        // Crio o Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DailyInfoService::class.java)
        val callback = service.getAll()

        callback.enqueue(object : Callback<DailyInfo> {
            override fun onFailure(call: Call<DailyInfo>?, t: Throwable?) {
                if (t != null) {
                    var dailyInfo2 = context?.let { AppDatabase.getDatabase(it).dailyInfoDAO().getAll() }

                    if (dailyInfo2 != null) {
                        date.text = dailyInfo2.date
                        new_confirmed.text = "${getString(R.string.new_confirmed_cases)}" + " " + dailyInfo2.newConfirmed.toString()
                        active.text = "${getString(R.string.active_cases)}" + " " + dailyInfo2.active.toString()
                        confirmed.text = "${getString(R.string.total_confirmed_cases)}" + " " + dailyInfo2.confirmed.toString()
                        recovered.text = "${getString(R.string.total_recovered_cases)}" + " " + dailyInfo2.recovered.toString()
                        hospitalized.text = "${getString(R.string.hospitalized)}" + " " + dailyInfo2.hospitalized.toString()
                        uci_hospitalized.text = "${getString(R.string.uci_hospitalized)}" + " " + dailyInfo2.UCIhospitalized.toString()
                        total_deaths.text = "${getString(R.string.total_number_of_deaths)}" + " " + dailyInfo2.totalDeaths.toString()
                        rt.text = "${getString(R.string.rt)}" + " " + dailyInfo2.rt.toString()
                    }
                }
            }

            override fun onResponse(call: Call<DailyInfo>?, response: Response<DailyInfo>?) {

                val infoResponse = response?.body()!!

                var dailyInfo = DailyInfo(infoResponse.date, infoResponse.newConfirmed, infoResponse.active,
                    infoResponse.confirmed, infoResponse.recovered, infoResponse.hospitalized,
                    infoResponse.UCIhospitalized, infoResponse.totalDeaths, infoResponse.rt)

                date.text = infoResponse.date
                new_confirmed.text = "${getActivity()?.getString(R.string.new_confirmed_cases)}" + " " + infoResponse.newConfirmed.toString()
                active.text = "${getActivity()?.getString(R.string.active_cases)}" + " " + infoResponse.active.toString()
                confirmed.text = "${getActivity()?.getString(R.string.total_confirmed_cases)}" + " " + infoResponse.confirmed.toString()
                recovered.text = "${getActivity()?.getString(R.string.total_recovered_cases)}" + " " + infoResponse.recovered.toString()
                hospitalized.text = "${getActivity()?.getString(R.string.hospitalized)}" + " " + infoResponse.hospitalized.toString()
                uci_hospitalized.text = "${getActivity()?.getString(R.string.uci_hospitalized)}" + " " + infoResponse.UCIhospitalized.toString()
                total_deaths.text = "${getActivity()?.getString(R.string.total_number_of_deaths)}" + " " + infoResponse.totalDeaths.toString()
                rt.text = "${getActivity()?.getString(R.string.rt)}" + " " + infoResponse.rt.toString()

                context?.let { AppDatabase.getDatabase(it).dailyInfoDAO().deleteAll() }
                context?.let { AppDatabase.getDatabase(it).dailyInfoDAO().insertAll(dailyInfo) }
            }
        })

        ButterKnife.bind(this, view)
        return view
    }

    private fun getConnectionStatusNow(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}
