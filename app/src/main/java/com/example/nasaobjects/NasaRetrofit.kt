package com.example.nasaobjects
import io.reactivex.Observable
import retrofit2.http.GET

interface NasaRetrofit {
    @GET("gh4g-9sfh.json")
    fun getAllNasaObjects(): Observable<List<NasaObject>>;
}