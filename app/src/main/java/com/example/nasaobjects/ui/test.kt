package com.example.nasaobjects.ui

import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import java.util.*

class test<spec> {
    var requireTls12 = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .build()
    var client = OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(requireTls12))
            .build()
}