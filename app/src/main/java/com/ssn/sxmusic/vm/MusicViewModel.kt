package com.ssn.sxmusic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.sxmusic.database.SongDatabase
import com.ssn.sxmusic.media.SongManager
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.model.SongsX
import com.ssn.sxmusic.network.MusicClient
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : ViewModel() {

    private var _musics = MutableLiveData<ArrayList<Song>>()
    val listMusic: LiveData<ArrayList<Song>>
        get() = _musics

    private var _album = MutableLiveData<ArrayList<SongsX>>()
    val album: LiveData<ArrayList<SongsX>>
        get() = _album

    private val songDao = SongDatabase.getStudentDatabase(application).getSongDao()

    fun getFavoriteSongs(): LiveData<List<Song>> = songDao.getAllSong()


    suspend fun addSong(s: Song) {
        songDao.insertSong(s)
    }

    suspend fun deleteSong(name: String) {
        songDao.deleteSongByName(name)
    }

    suspend fun findSongByName(name: String): Boolean {
        if (songDao.findSongByName(name)  != null) {
            return true
        }
        return false
    }


    fun getAllMusic() {
        viewModelScope.launch {
            val musics = MusicClient.invoke().getAllSong()
            SongManager.Musics = musics
            SongManager.getAllSong()
        }

    }

    fun getAlbum() {
        val s = SongManager.getAlbum()
        _album.postValue(s as ArrayList<SongsX>?)
    }

    fun getSongs() {
        _musics.postValue(SongManager.allSong)
    }

}