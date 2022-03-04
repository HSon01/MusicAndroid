package com.ssn.sxmusic.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    private var musics: Call<Musics> = MusicClient.invoke().getAllSong0()
    private val songDao = SongDatabase.getStudentDatabase(application).getSongDao()

    private fun getSongs(): LiveData<ArrayList<Song>> = SongManager.listMusic
    val listMusic: LiveData<ArrayList<Song>>
        get() = getSongs()

    private fun getAlbums(): LiveData<ArrayList<SongsX>> = SongManager.listAlbum
    val album: LiveData<ArrayList<SongsX>>
        get() = getAlbums()

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
        viewModelScope.launch {
            musics.enqueue(object : Callback<Musics> {
                override fun onResponse(call: Call<Musics>, response: Response<Musics>) {
                    response.body().let {
                        if (it != null) {
                            SongManager.musics = it
                            SongManager.getSongAndAlbum()
                            Log.d("AGT", "${SongManager.allSong.size}")
                        }
                    }
                }

                override fun onFailure(call: Call<Musics>, t: Throwable) {
                    Log.d("AGT", "CALL API ERROR $t")
                    musics = MusicClient.invoke().getAllSong0()
                }
            })
        }
    }
}