package pt.ulusofona.deisi.a2020.cm.g7.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.VaccineInfo
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.AppDatabase
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class VaccineInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_vaccine_info, container, false)

        val date2 = view.findViewById<View>(R.id.date2) as TextView
        val doses = view.findViewById<View>(R.id.doses) as TextView
        val doses1 = view.findViewById<View>(R.id.doses1) as TextView
        val doses1_novas = view.findViewById<View>(R.id.doses1_novas) as TextView
        val doses1_percentagem = view.findViewById<View>(R.id.doses1_percentagem) as TextView
        val doses2 = view.findViewById<View>(R.id.doses2) as TextView
        val doses2_novas = view.findViewById<View>(R.id.doses2_novas) as TextView
        val doses2_percentagem = view.findViewById<View>(R.id.doses2_percentagem) as TextView

        if(getConnectionStatusNow() == true) {

            Thread(Runnable {
                // Para ler cada linha
                val linhas = ArrayList<String>()

                try {
                    // Crio o URL para o página no github
                    val url = URL("https://raw.githubusercontent.com/dssg-pt/covid19pt-data/master/vacinas.csv")

                    // Para a conexão
                    val conn = url.openConnection() as HttpURLConnection
                    val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                    var str: String

                    while (`in`.readLine().also { str = it } != null) {
                        linhas.add(str)
                    }

                    `in`.close()
                } catch (e: java.lang.Exception) {
                    Log.d("Erro: ", e.toString())
                }

                Handler(Looper.getMainLooper()).post(Runnable() {
                    val tokens: List<String> = linhas[linhas.size - 1].split(",")

                    var vaccineInfo = VaccineInfo(
                        tokens[0], tokens[1], tokens[3], tokens[4],
                        ((100 * tokens[3].toInt()) / 10286000).toString(), tokens[5], tokens[6],
                        ((100 * tokens[5].toInt()) / 10286000).toString()
                    )

                    date2.text = tokens[0]
                    doses.text = "${getActivity()?.getString(R.string.total_vaccine_doses)}" + " " + tokens[1]
                    doses1.text = "${getActivity()?.getString(R.string.total_vaccine_doses)}" + " " + tokens[3]
                    doses1_novas.text = "${getActivity()?.getString(R.string.daily_vaccine_doses)}" + " " + tokens[4]
                    doses1_percentagem.text = "${getActivity()?.getString(R.string.percentage_of_vaccine_doses)}" + " " + ((100 * tokens[3].toInt()) / 10286000).toString() + "%"
                    doses2.text = "${getActivity()?.getString(R.string.total_vaccine_doses)}" + " " + tokens[5]
                    doses2_novas.text = "${getActivity()?.getString(R.string.daily_vaccine_doses)}" + " " + tokens[6]
                    doses2_percentagem.text = "${getActivity()?.getString(R.string.percentage_of_vaccine_doses)}" + " " + ((100 * tokens[5].toInt()) / 10286000).toString() + "%"

                    context?.let { AppDatabase.getDatabase(it).vaccineInfoDAO().deleteAll() }
                    context?.let { AppDatabase.getDatabase(it).vaccineInfoDAO().insertAll(vaccineInfo) }
                })

            }).start()

        } else {
            var vaccineInfo2 = context?.let { AppDatabase.getDatabase(it).vaccineInfoDAO().getAll() }

            if (vaccineInfo2 != null) {
                date2.text = vaccineInfo2.date2
                doses.text = "${getString(R.string.total_vaccine_doses)}" + " " + vaccineInfo2.totalVaccineDoses
                doses1.text = "${getString(R.string.total_vaccine_doses)}" + " " + vaccineInfo2.totalFirstVaccineDoses
                doses1_novas.text = "${getString(R.string.daily_vaccine_doses)}" + " " + vaccineInfo2.dailyFirstVaccineDoses
                doses1_percentagem.text = "${getString(R.string.percentage_of_vaccine_doses)}" + " " + vaccineInfo2.percentageFirstVaccineDoses + "%"
                doses2.text = "${getString(R.string.total_vaccine_doses)}" + " " + vaccineInfo2.totalSecondVaccineDoses
                doses2_novas.text = "${getString(R.string.daily_vaccine_doses)}" + " " + vaccineInfo2.dailySecondVaccineDoses
                doses2_percentagem.text = "${getString(R.string.percentage_of_vaccine_doses)}" + " " + vaccineInfo2.percentageSecondVaccineDoses + "%"
            }

            Toast.makeText(activity as Context, "${getString(R.string.no_connection)}", Toast.LENGTH_LONG).show()
        }

        return view
    }

    private fun getConnectionStatusNow(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}
