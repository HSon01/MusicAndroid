package com.ssn.sxmusic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MusicClient {

    //private const val BASE_API = "127.0.0.1/"
    private const val BASE_API = "http://192.168.1.11:8080/"
    private var retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    operator fun invoke(): MusicApi = retrofit.create(MusicApi::class.java)

    val api : MusicApi by lazy {
        retrofit.create(MusicApi::class.java)
    }

}