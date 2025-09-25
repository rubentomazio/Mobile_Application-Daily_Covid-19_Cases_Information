package pt.ulusofona.deisi.a2020.cm.g7.data.remote.responses

import com.google.gson.annotations.SerializedName

data class CountiesResponse(
    @SerializedName("distrito") var district: String? = null,
    @SerializedName("data") var date: String? = null,
    @SerializedName("incidencia_risco") val incidenciaRisco: String? = null,
    var incidencia: Int = 0,
    var ars: String? = null
)