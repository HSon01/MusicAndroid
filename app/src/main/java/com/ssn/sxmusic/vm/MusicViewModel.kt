package com.ssn.sxmusic.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssn.sxmusic.media.SongManager
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.model.SongsX
import com.ssn.sxmusic.network.MusicClient
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {

    private var _musics = MutableLiveData<ArrayList<Song>>()
    val listMusic: LiveData<ArrayList<Song>>
        get() = _musics

    private var _album = MutableLiveData<ArrayList<SongsX>>()
    val album: LiveData<ArrayList<SongsX>>
        get() = _album

    fun getAllListMusic() {
        val job = viewModelScope.launch {
            val musics = MusicClient.invoke().getAllSong()
            SongManager.Musics = musics
            SongManager.getAllSong()
        }
        job.invokeOnCompletion {
            _musics.postValue(SongManager.allSong)
            Log.d("TAG", "SongManager.allSong")
        }
    }

    fun getAlbum() {
        val s = SongManager.getAlbum()
        _album.postValue(s as ArrayList<SongsX>?)
    }

}