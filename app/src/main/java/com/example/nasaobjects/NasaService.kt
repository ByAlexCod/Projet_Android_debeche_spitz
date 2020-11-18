package com.example.nasaobjects

import android.os.Build
import androidx.annotation.RequiresApi
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object NasaService {
    private var retrofit : Retrofit = Retrofit.Builder()
        .baseUrl("https://data.nasa.gov/resource/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()



    @RequiresApi(Build.VERSION_CODES.N)
    fun getNasaObjects(): Observable<List<NasaObject>> {
        val nasaRetrofit: NasaRetrofit = retrofit.create(NasaRetrofit::class.java)
        return nasaRetrofit.getAllNasaObjects()
    }
}