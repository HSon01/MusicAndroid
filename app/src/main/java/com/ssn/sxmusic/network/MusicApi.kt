package com.ssn.sxmusic.network

import com.ssn.sxmusic.model.Musics
import retrofit2.Call
import retrofit2.http.GET

interface MusicApi {
//    @GET("ping")
//    suspend fun getAllSong(): Musics
//https://api.apify.com/v2/key-value-stores/Y7WXaCojKGTuaQj0X/records/INPUT
    @GET("7cf4c62c23ee51fb4a47")
    suspend fun getAllSong(): Musics

//    @GET("25a8218c041266249b50")
//    fun getAllSong0(): Call<Musics>

    @GET("Y7WXaCojKGTuaQj0X/records/INPUT")
    fun getAllSong0(): Call<Musics>

}