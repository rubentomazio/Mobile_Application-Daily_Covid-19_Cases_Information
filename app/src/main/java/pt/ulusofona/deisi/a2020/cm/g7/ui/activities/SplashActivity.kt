package pt.ulusofona.deisi.a2020.cm.g7.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pt.ulusofona.deisi.a2020.cm.g7.R
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.Countie
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.AppDatabase
import pt.ulusofona.deisi.a2020.cm.g7.data.remote.services.CountieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var location = false

class SplashActivity : AppCompatActivity() {

    var mDelayHandler: Handler? = null
    // 3 segundos
    val SPLASH_TIMER: Long = 3000
    var BaseUrl = "https://covid19-api.vost.pt/"
    val REQUEST_CODE = 123

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun init() {
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_TIMER)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        location = true
                        init()
                    } else {
                        init()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getData()

        val permissionCheck: Int = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        } else {
            location = true
            init()
        }
    }

    override fun onStart() {
        super.onStart()
    }


    private fun getData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val endpoint = retrofit.create(CountieService::class.java)
        val callback = endpoint.getCounties()

        callback.enqueue(object : Callback<List<Countie>> {
            override fun onFailure(call: Call<List<Countie>>?, t: Throwable?) {
                countiesList = AppDatabase.getDatabase(this@SplashActivity).countieDAO().getAll()
                countiesList.forEach {
                    if(!searchList.contains(it.countie)) {
                        searchList.add(it.countie)
                    }
                }
            }

            override fun onResponse(call: Call<List<Countie>>?, response: Response<List<Countie>>?) {
                if (response != null) {
                    countiesList = response.body()!! as MutableList<Countie>
                    countiesList.forEach {
                        if(!searchList.contains(it.countie)) {
                            searchList.add(it.countie)
                        }
                    }
                }
            }
        })
    }
}