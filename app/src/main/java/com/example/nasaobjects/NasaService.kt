package com.example.nasaobjects

import android.os.Build
import androidx.annotation.RequiresApi
import io.reactivex.Observable
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


object NasaService {
    private var requireTls12 = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .build()
    private var client = OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(requireTls12))
            .build()

    private var retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://data.nasa.gov/resource/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @RequiresApi(Build.VERSION_CODES.N)
    fun getNasaObjects(): Observable<List<NasaObject>> {
        val nasaRetrofit: NasaRetrofit = retrofit.create(NasaRetrofit::class.java)
        return nasaRetrofit.getAllNasaObjects()
    }
}