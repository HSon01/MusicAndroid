package com.ssn.sxmusic.media

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssn.sxmusic.model.Musics
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.model.SongsX

object SongManager {
    var Album = arrayListOf<SongsX>()
    var allSong = arrayListOf<Song>()
    var musics: Musics? = null

    private var allSongs = MutableLiveData<ArrayList<Song>>()
    val listMusic: LiveData<ArrayList<Song>>
        get() = allSongs

    private var albums = MutableLiveData<ArrayList<SongsX>>()
    val listAlbum: LiveData<ArrayList<SongsX>>
        get() = albums


    private var top100_AM = arrayListOf<Song>()
    private var top100_CA = arrayListOf<Song>()
    private var top100_KL = arrayListOf<Song>()
    private var top100_VN = arrayListOf<Song>()

    private fun getAMSong(): List<Song> {
        val list = musics?.songs?.top100_AM
        list?.forEach {
            allSong.addAll(it.songs)
            Album.addAll(listOf(it))
            Log.d("KKL", "Name top100_AM ${it.name}")
            top100_AM.addAll(it.songs)
        }
        return top100_AM
    }

    private fun getCASong(): List<Song> {
        val list = musics?.songs?.top100_CA
        list?.forEach {
            allSong.addAll(it.songs)
            Album.addAll(listOf(it))
            Log.d("KKL", "Name top100_CA ${it.name}")
            top100_CA.addAll(it.songs)
        }
        return top100_CA
    }

    private fun getVNSong(): List<Song> {
        val list = musics?.songs?.top100_VN
        list?.forEach {
            allSong.addAll(it.songs)
            Album.addAll(listOf(it))
            Log.d("KKL", "Name top100_VN ${it.name}")
            top100_VN.addAll(it.songs)
        }
        return top100_VN
    }

    private fun getKLSong(): List<Song> {
        val list = musics?.songs?.top100_KL
        list?.forEach {
            allSong.addAll(it.songs)
            Log.d("KKL", "Name top100_KL ${it.name}")
            Album.addAll(listOf(it))
            top100_KL.addAll(it.songs)
        }
        return top100_KL
    }

    fun getSongAndAlbum() {
        getVNSong()
        getAMSong()
        getCASong()
        getKLSong()
        allSongs.postValue(allSong)
        albums.postValue(Album)
    }

//    fun getAlbum(): List<SongsX> {
//        Log.d("KKL", "ALBUM SIZE${Album.size}")
//        return Album
//    }

}