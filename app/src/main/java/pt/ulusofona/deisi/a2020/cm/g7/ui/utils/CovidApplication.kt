package pt.ulusofona.deisi.a2020.cm.g7.ui.utils

import android.app.Application
import pt.ulusofona.deisi.a2020.cm.g7.data.sensors.battery.Battery
import pt.ulusofona.deisi.a2020.cm.g7.data.sensors.connectivity.Connectivity
import pt.ulusofona.deisi.a2020.cm.g7.data.sensors.location.FusedLocation

class CovidApplication : Application() {

    override fun onCreate() {

        super.onCreate()

        // Lê os sensores e notifica os observers
        FusedLocation.start(this)
        Battery.start(this)
        Connectivity.start(this)
    }
}