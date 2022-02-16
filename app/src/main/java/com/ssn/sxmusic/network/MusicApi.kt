package com.ssn.sxmusic.network

import com.ssn.sxmusic.model.Musics
import retrofit2.Call
import retrofit2.http.GET

interface MusicApi {
    @GET("ping")
    suspend fun getAllSong(): Musics

    @GET("ping")
    fun getAllSong1(): Call<Musics>
}