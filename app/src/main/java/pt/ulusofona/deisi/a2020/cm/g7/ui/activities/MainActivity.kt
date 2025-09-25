package pt.ulusofona.deisi.a2020.cm.g7.ui.activities

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.location.LocationManagerCompat
import androidx.core.view.GravityCompat
import butterknife.ButterKnife
import com.google.android.gms.location.LocationResult
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.Countie
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.AppDatabase
import pt.ulusofona.deisi.a2020.cm.g7.data.sensors.battery.Battery
import pt.ulusofona.deisi.a2020.cm.g7.data.sensors.battery.OnBatteryCapacityListener
import pt.ulusofona.deisi.a2020.cm.g7.data.sensors.location.FusedLocation
import pt.ulusofona.deisi.a2020.cm.g7.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.deisi.a2020.cm.g7.ui.listeners.OnNavigationListener
import pt.ulusofona.deisi.a2020.cm.g7.ui.utils.NavigationManager
import java.io.IOException
import java.util.*

const val PREFERENCE_APPLIED_THEME = "APPLIED_THEME"
const val PREFERENCE_QUEUED_THEME = "QUEUED_THEME"
var countiesList = listOf<Countie>()
lateinit var countieClicked: Countie
var searchList = mutableListOf<String>()

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnNavigationListener, OnBatteryCapacityListener, OnLocationChangedListener {

    // Recebe a localização do utilizador
    private var userLocation: Location? = null

    // Muda para o Dark Mode quando a bateria é igual ou está abaixo dos 20%
    private fun validateThemeBattery(capacity: Int) {

        CoroutineScope(Dispatchers.Default).launch {

            val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
            val sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
            val value = sharedPreferences2.getBoolean("themes_automatically", false)

            // Vê se a bateria foi carregada
            if (capacity > 20 || value) {
                sharedPreferences.edit().putInt(PREFERENCE_QUEUED_THEME, R.style.LightTheme).apply()
            }

            if (!value && capacity <= 20) {
                withContext(Dispatchers.Main) {
                    sharedPreferences.edit().putInt(PREFERENCE_QUEUED_THEME, R.style.DarkTheme).apply()
                }
            }
        }
    }

    private fun validateThemes() {

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val appliedTheme = sharedPreferences.getInt(PREFERENCE_APPLIED_THEME, R.style.LightTheme)
        val queuedTheme = sharedPreferences.getInt(PREFERENCE_QUEUED_THEME, R.style.LightTheme)

        if(appliedTheme != queuedTheme) {

            // Recria a atividade para ver as mudanças
            recreate()
        }
    }

    private fun updateTheme() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val queuedTheme = sharedPreferences.getInt(PREFERENCE_QUEUED_THEME, R.style.LightTheme)
        sharedPreferences.edit().putInt(PREFERENCE_APPLIED_THEME, queuedTheme).apply()
        setTheme(queuedTheme)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Aqui escolho o fragmento para onde quero ir e verifico se o esquema de cores muda
        when(item.itemId) {

            R.id.navigation_daily_info -> {
                NavigationManager.goToDailyInfoFragment(supportFragmentManager)
                validateThemes()
                getDanger()
            }

            R.id.navigation_counties -> {
                NavigationManager.goToCountiesFragment(supportFragmentManager)
                validateThemes()
                getDanger()
            }

            R.id.navigation_covid_tests -> {
                NavigationManager.goToCovidTestsFragment(supportFragmentManager)
                validateThemes()
                getDanger()
            }

            R.id.navigation_contacts -> {
                NavigationManager.goToContactsFragment(supportFragmentManager)
                validateThemes()
                getDanger()
            }

            R.id.navigation_vaccine_info -> {
                NavigationManager.goToVaccineInfoFragment(supportFragmentManager)
                validateThemes()
                getDanger()
            }

            R.id.navigation_settings -> {
                NavigationManager.goToSettingsFragment(supportFragmentManager)
                validateThemes()
                getDanger()
            }

            R.id.navigation_exit -> {
                finish()
            }
        }

        // Fechar o drawer
        drawer.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onBackPressed() {

        when {
            // Se o drawer está aberto, fechar o drawer
            drawer.isDrawerOpen(GravityCompat.START) -> drawer.closeDrawer(GravityCompat.START)

            // Se existe apenas um fragmento na stack, finish
            supportFragmentManager.backStackEntryCount == 1 -> finish()

            else -> {
                validateThemes()
                getDanger()
                super.onBackPressed()
            }
        }
    }

    private fun setupDrawerMenu() {

        // Botão para abrir e fechar
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close)

        // Inscrever a atividade na interface (navegar para o fragmento)
        navigation_drawer.setNavigationItemSelectedListener(this)

        // Botão de alternância de inscrição para a interface do drawer (alterna o estado)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {
        return savedInstanceState != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTheme()
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupDrawerMenu()

        if(!screenRotated(savedInstanceState)) {
            NavigationManager.goToDailyInfoFragment(supportFragmentManager)
        }

        AppDatabase.getDatabase(this@MainActivity).countieDAO().insertAll(countiesList)

        search_button.setOnClickListener {
            search_write.visibility = View.VISIBLE
            search_back.visibility = View.VISIBLE
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, searchList)
        val actv = findViewById<View>(R.id.search_write) as AutoCompleteTextView
        //Começa a mostrar os concelhos a partir da primeira letra
        actv.threshold = 1
        actv.setAdapter(adapter)
        actv.setTextColor(Color.WHITE)

        search_back.setOnClickListener {
            search_write.visibility = View.GONE
            search_back.visibility = View.GONE
        }

        getDanger()
    }

    override fun onStart() {
        Battery.registerListener(this)
        FusedLocation.registerActivityListener(this)

        search_write.onItemClickListener =
            AdapterView.OnItemClickListener() { adapterView, view, i, l ->

                val nome: String = adapterView.getItemAtPosition(i) as String

                countiesList.forEach {
                    if (nome.equals(it.countie)) {
                        countieClicked = it
                    }
                }

                search_write.visibility = View.GONE
                search_back.visibility = View.GONE
                val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                search_write.setText("")
                supportFragmentManager.let { NavigationManager.goToCountieSearchFragment(it) }
            }

        super.onStart()
    }

    override fun onStop() {
        Battery.unregisterListener()
        FusedLocation.unregisterActivityListener()
        super.onStop()
    }

    override fun onBatteryCapacityListener(capacity: Int) {
        validateThemeBattery(capacity)
    }

    override fun onLocationChanged(locationResult: LocationResult) {
        this.userLocation = locationResult.lastLocation
    }

    override fun onNavigateToCovidTestForm(args: Bundle?) {
        validateThemes()
        getDanger()
        NavigationManager.goToCovidTestFormFragment(supportFragmentManager, args)
    }

    fun getDanger(){
        if(location == true) {

            if (isLocationEnabled(this) == true) {

                val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {

                    val location: Location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val longitude = location.longitude
                    val latitude = location.latitude

                    if (getCountry(this, latitude, longitude) != "Portugal") {
                        danger.text = "${getString(R.string.out)}"
                        danger.setTextColor(getColor(R.color.black))
                        danger.setBackgroundColor(getColor(R.color.lightGrey))
                    } else {

                        val city = getCity(this, latitude, longitude)
                        var city2 = ""

                        if (city == "Lisbon") {
                            city2 = "LISBOA"
                        } else if (city == "Aveiro District") {
                            city2 = "AVEIRO"
                        } else if (city == "Viana do Castelo District") {
                            city2 = "VIANA DO CASTELO"
                        } else {
                            city2 = city.toUpperCase(Locale.ROOT)
                        }

                        countiesList.forEach {
                            if (city2.equals(it.countie)) {
                                danger.text = "${getString(R.string.danger)}"
                                if (it.incidenciaRisco == "Baixo a Moderado") {
                                    danger.setBackgroundColor(getColor(R.color.green))
                                    danger.setTextColor(getColor(R.color.white))
                                } else if (it.incidenciaRisco == "Moderado") {
                                    danger.setBackgroundColor(getColor(R.color.yellow))
                                    danger.setTextColor(getColor(R.color.black))
                                } else if (it.incidenciaRisco == "Elevado") {
                                    danger.setBackgroundColor(getColor(R.color.orange))
                                    danger.setTextColor(getColor(R.color.white))
                                } else if (it.incidenciaRisco == "Muito Elevado") {
                                    danger.setBackgroundColor(getColor(R.color.red))
                                    danger.setTextColor(getColor(R.color.white))
                                } else if (it.incidenciaRisco == "Extremamente Elevado") {
                                    danger.setBackgroundColor(getColor(R.color.dark_red))
                                    danger.setTextColor(getColor(R.color.white))
                                }
                            }
                        }
                    }
                }
            } else {
                danger.text = "Offline"
                danger.setTextColor(getColor(R.color.black))
                danger.setBackgroundColor(getColor(R.color.lightGrey))
            }
        } else {
            danger.text = "${getString(R.string.denied)}"
            danger.setTextColor(getColor(R.color.black))
            danger.setBackgroundColor(getColor(R.color.lightGrey))
        }
    }

    fun getCity(context: Context?, latitude: Double, longituge: Double): String {

        var city= ""

        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longituge, 1)
            if (addresses != null && addresses.size > 0) {
                val country = addresses[0].countryName
                if(country == "Portugal"){
                    city = addresses[0].adminArea
                }
                Log.d(TAG, "Country: " + country);
                Log.d(TAG, "City: " + city);
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return city
    }

    fun getCountry(context: Context?, latitude: Double, longituge: Double): String {

        var country= ""

        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longituge, 1)
            if (addresses != null && addresses.size > 0) {
                country = addresses[0].countryName
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return country
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }
}
