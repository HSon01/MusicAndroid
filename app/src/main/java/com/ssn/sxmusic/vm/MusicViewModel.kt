package com.ssn.sxmusic.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssn.sxmusic.database.SongDao
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.media.SongManager
import com.ssn.sxmusic.model.Musics
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.model.SongsX
import com.ssn.sxmusic.network.MusicApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    application: Application,
    private val songDao: SongDao,
    private val musicNetWork: MusicApi
) : AndroidViewModel(application) {
    //    private val songDao = SongDatabase.getStudentDatabase(application).getSongDao()


    init {
        getAllMusic()
    }

    private fun getSongs(): LiveData<ArrayList<Song>> = SongManager.listMusic
    val listMusic: LiveData<ArrayList<Song>>
        get() = getSongs()

    private fun getAlbums(): LiveData<ArrayList<SongsX>> = SongManager.listAlbum
    val album: LiveData<ArrayList<SongsX>>
        get() = getAlbums()

    private val _isActive = MutableLiveData<Boolean>()
    val isActive: LiveData<Boolean>
        get() = _isActive


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

    fun setSongCurrent(s: Song) {
        viewModelScope.launch {
            MediaController.findAndSetSong(s)
        }
    }


    private fun getAllMusic() {
        viewModelScope.launch {
//            var musics: Call<Musics> = MusicClient.invoke().getAllSong0()
            var musics: Call<Musics> = musicNetWork.getSongs()
            musics.enqueue(object : Callback<Musics> {
                override fun onResponse(call: Call<Musics>, response: Response<Musics>) {
                    if (response.isSuccessful) {
                        _isActive.postValue(true)
                        response.body().let {
                            if (it != null) {
                                SongManager.musics = it
                                SongManager.getSongAndAlbum()
                                Log.d("AGT", "${SongManager.allSong.size}")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Musics>, t: Throwable) {
                    Log.d("AGT", "CALL API ERROR $t")
                    viewModelScope.launch {
                        musics = musicNetWork.getSongs()
                    }
                }
            })
        }
    }

    fun searchMusics(text: String):List<Song> {
        var list = arrayListOf<Song>()
        try {
            if (text.isEmpty()){
                list = listMusic.value!!
            }
            listMusic.value?.forEach {
                if (it.title.lowercase(Locale.getDefault()).trim()
                        .startsWith(text)
                ) {
                    list.add(it)
                }
            }
        }catch (e:Exception){
            Log.d("AGT", "ERROR")
        }
        return list
    }
}