package pt.ulusofona.deisi.a2020.cm.g7.data.remote.services

import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.Countie
import retrofit2.Call
import retrofit2.http.GET

interface CountieService {

    @GET("Requests/get_last_update_counties")
    fun getCounties(): Call<List<Countie>>
}