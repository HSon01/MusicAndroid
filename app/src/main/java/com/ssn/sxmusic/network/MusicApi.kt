package com.ssn.sxmusic.network

import com.ssn.sxmusic.model.Musics
import retrofit2.Call
import retrofit2.http.GET

interface MusicApi {
    //    @GET("ping")
//    suspend fun getAllSong(): Musics
    @GET("7cf4c62c23ee51fb4a47")
    suspend fun getAllSong(): Musics

    @GET("7cf4c62c23ee51fb4a47")
    fun getAllSong0(): Call<Musics>
}