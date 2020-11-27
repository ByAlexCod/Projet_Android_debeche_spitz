package com.example.nasaobjects

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import io.reactivex.Observable
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit


object NasaService {
    private var requireTls12 = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .allEnabledTlsVersions()
        .allEnabledCipherSuites()
            .build()
    private var client = OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(requireTls12))
            .callTimeout(30, TimeUnit.SECONDS)
            .addInterceptor {
            val request: Request = it.request()

            var response: Response = it.proceed(request)
            var tryCount = 0
            while (!response.isSuccessful() && tryCount < 10) {
                Log.d("intercept", "Request is not successful - $tryCount")
                tryCount++
                response = it.proceed(request)
            }

            return@addInterceptor response
        }
            .build()



    @RequiresApi(Build.VERSION_CODES.O)
    var gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java,
        JsonDeserializer<Any?> { json, type, jsonDeserializationContext ->
            LocalDate.parse(json.asJsonPrimitive.asString.split("T")[0])
        }).create()

    private var retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://data.nasa.gov/resource/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @RequiresApi(Build.VERSION_CODES.N)
    fun getNasaObjects(): Observable<List<NasaObject>> {
        val nasaRetrofit: NasaRetrofit = retrofit.create(NasaRetrofit::class.java)
        return nasaRetrofit.getAllNasaObjects()
    }

}
