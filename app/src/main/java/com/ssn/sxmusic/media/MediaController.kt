package com.ssn.sxmusic.media

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.Const.MEDIA_IDLE
import com.ssn.sxmusic.util.Const.MEDIA_LOOP_ALL
import com.ssn.sxmusic.util.Const.MEDIA_PAUSED
import com.ssn.sxmusic.util.Const.MEDIA_PLAYING
import com.ssn.sxmusic.util.Const.MEDIA_STOPPED
import com.ssn.sxmusic.util.Const.SERVICE_SEND_DATA
import com.ssn.sxmusic.util.PrefControllerSingleton

@SuppressLint("StaticFieldLeak")
object MediaController {
    var songs = arrayListOf<Song>()
    var mediaPlayer: MediaPlayer? = null
    private var currentPositionS = 0
    private lateinit var context: Context
    var currentSong: Song? = null
    var mediaState = MEDIA_IDLE
    private val sharedPrefControl = PrefControllerSingleton

    fun mediaController(c: Context) {
        context = c
        sharedPrefControl.prefController(c)
        initMediaPlay()
    }

    fun setListSong(l: List<Song>) {
        songs.clear()
        songs.addAll(l)
    }

    private fun initMediaPlay() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnCompletionListener {
            if (isLoop()) {
                playPauseSong(true)
            } else {
                nextSong()
            }
        }
    }


    fun setCurrentSong(index: Int) {
        currentPositionS = index
        currentSong = songs[currentPositionS]
    }


    fun playPauseSong(
        isNew: Boolean = false,
        nextSong: Boolean = false,
        previousSong: Boolean = false,
    ) {
        val intent = Intent(SERVICE_SEND_DATA)
        val song = currentSong
        if (isNew || mediaState == MEDIA_IDLE || mediaState == MEDIA_STOPPED) {
            try {
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(song?.music)
                mediaPlayer?.prepare()
                mediaPlayer?.start()
                mediaState = MEDIA_PLAYING
            } catch (ex: Exception) {
                songs.removeAt(currentPositionS)
                when {
                    nextSong -> {
                        nextSong()
                    }
                    previousSong -> {
                        previousSong()
                    }
                    else -> {
                        nextSong()
                    }
                }
            }
        } else if (mediaState == MEDIA_PLAYING) {
            mediaPlayer?.pause()
            mediaState = MEDIA_PAUSED
        } else if (mediaState == MEDIA_PAUSED) {
            mediaPlayer?.start()
            mediaState = MEDIA_PLAYING
        }
        context.sendBroadcast(intent)
    }

    fun getIsPlay(): Int {
        return mediaState
    }


    fun nextSong() {
        currentPositionS++
        if (currentPositionS >= songs.size - 1) {
            currentPositionS = 0
        }
        currentSong = songs[currentPositionS]
        playPauseSong(true, nextSong = true)
    }


    fun previousSong() {
        if (currentPositionS <= 0) {
            currentPositionS = songs.size - 1
        } else {
            currentPositionS--
        }
        currentSong = songs[currentPositionS]
        playPauseSong(true, previousSong = true)
    }

    fun getDuration(): Int? {
        return mediaPlayer?.duration
    }

    fun getCurrentPosition(): Int? {
        return mediaPlayer?.currentPosition
    }

    fun findSongByPosition(s: Song): Int {
        for (i in 0 until songs.size) {
            if (songs[i] == s) {
                return i
            }
        }
        return -1
    }

    fun setPosition(time: Int) {
        mediaPlayer?.seekTo(time)
    }

    private fun isLoop(): Boolean {
        val loop = sharedPrefControl.getMediaLoop(Const.MEDIA_CURRENT_STATE_LOOP, MEDIA_LOOP_ALL)
        Log.d("TAG", "{isLoop} $loop")
        when (loop) {
            Const.MEDIA_LOOP_ONE -> {
                return true
            }
            MEDIA_LOOP_ALL -> {
                return false
            }
        }
        return false
    }
}