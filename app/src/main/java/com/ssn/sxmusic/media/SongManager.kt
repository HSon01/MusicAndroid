package com.ssn.sxmusic.media

import android.util.Log
import com.ssn.sxmusic.model.Musics
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.model.SongsX

object SongManager {
    var allSong = arrayListOf<Song>()
    var musics: Musics? = null
    var Album = arrayListOf<SongsX>()
    private var top100_AM = arrayListOf<Song>()
    private var top100_CA = arrayListOf<Song>()
    private var top100_KL = arrayListOf<Song>()
    private var top100_VN = arrayListOf<Song>()

    private fun getAMSong(): List<Song> {
        val list = musics?.songs?.top100_AM
        list?.forEach {
            allSong.addAll(it.songs)
            Album.addAll(listOf(it))
            top100_AM.addAll(it.songs)
        }
        return top100_AM
    }

    private fun getCASong(): List<Song> {
        val list = musics?.songs?.top100_CA
        list?.forEach {
            allSong.addAll(it.songs)
            Album.addAll(listOf(it))
            top100_CA.addAll(it.songs)
        }
        return top100_CA
    }

    private fun getVNSong(): List<Song> {
        val list = musics?.songs?.top100_VN
        list?.forEach {
            allSong.addAll(it.songs)
            Album.addAll(listOf(it))
            top100_VN.addAll(it.songs)
        }
        return top100_VN
    }

    private fun getKLSong(): List<Song> {
        val list = musics?.songs?.top100_KL
        list?.forEach {
            allSong.addAll(it.songs)
            Album.addAll(listOf(it))
            top100_KL.addAll(it.songs)
        }
        return top100_KL
    }

    fun getAllSong(): List<Song> {
        getVNSong()
        getAMSong()
        getCASong()
        getKLSong()
        return allSong
    }

    fun getAlbum(): List<SongsX> {
        Log.d("TAG GET ALBUM", "${Album.size}")
        return Album
    }

}