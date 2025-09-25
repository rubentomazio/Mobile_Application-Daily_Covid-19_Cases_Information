package pt.ulusofona.deisi.a2020.cm.g7.data.sensors.connectivity

interface OnConnectivityStatusListener {
    fun onConnectivityStatus(connected: Boolean)
}