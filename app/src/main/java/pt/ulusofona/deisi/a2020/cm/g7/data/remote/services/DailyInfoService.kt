package pt.ulusofona.deisi.a2020.cm.g7.data.remote.services

import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.DailyInfo
import retrofit2.Call
import retrofit2.http.GET

interface DailyInfoService {

    @GET("Requests/get_last_update")
    fun getAll(): Call<DailyInfo>
}