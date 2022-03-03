package com.ssn.sxmusic.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssn.sxmusic.database.SongDatabase
import com.ssn.sxmusic.media.SongManager
import com.ssn.sxmusic.model.Musics
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.model.SongsX
import com.ssn.sxmusic.network.MusicClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val musics: Call<Musics> = MusicClient.api.getAllSong0()
    private val songDao = SongDatabase.getStudentDatabase(application).getSongDao()

    private var _listMusic = MutableLiveData<ArrayList<Song>>()
    val listMusic: LiveData<ArrayList<Song>>
        get() = _listMusic

    private var _album = MutableLiveData<ArrayList<SongsX>>()
    val album: LiveData<ArrayList<SongsX>>
        get() = _album

    private var _startActivity = MutableLiveData<Boolean>()
    val startActivity: LiveData<Boolean>
        get() = _startActivity





    fun getFavoriteSongs(): LiveData<List<Song>> = songDao.getAllSong()

    suspend fun addSong(s: Song) {
        songDao.insertSong(s)
    }

    suspend fun deleteSong(name: String) {
        songDao.deleteSongByName(name)
    }

    suspend fun findSongByName(name: String): Boolean {
        if (songDao.checkExistSong(name)) {
            return true
        }
        return false
    }


    fun getAllMusic() {
//       val job = viewModelScope.launch {
//           musics = MusicClient.invoke().getAllSong()
//        }
//        job.invokeOnCompletion {
//            if(SongManager.musics != null){
//                SongManager.musics = musics
//                SongManager.getAllSong()
//            }
//        }
        viewModelScope.launch {
            musics.enqueue(object : Callback<Musics> {
                override fun onResponse(call: Call<Musics>, response: Response<Musics>) {
                    response.body().let {
                        if (it != null) {

                            SongManager.musics = it
                            _startActivity.postValue(true)
                            SongManager.getAllSong()

                            Log.d("AGT", "${SongManager.allSong.size}")
                        }
                    }
                }

                override fun onFailure(call: Call<Musics>, t: Throwable) {
                    Log.d("AGT ERROR", "CALL API ERROR")
                }
            })
        }
    }

    fun getAlbum() {
        val s = SongManager.getAlbum()
        _album.postValue(s as ArrayList<SongsX>?)
    }

    fun getSongs() {
        _listMusic.postValue(SongManager.allSong)
    }

}