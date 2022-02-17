package com.ssn.sxmusic.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ssn.sxmusic.R
import com.ssn.sxmusic.databinding.ActivityDetailSongBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.Const.ACTION_NEXT
import com.ssn.sxmusic.util.Const.ACTION_PAUSE
import com.ssn.sxmusic.util.Const.ACTION_PLAYING
import com.ssn.sxmusic.util.Const.ACTION_PREVIOUS
import com.ssn.sxmusic.util.Const.MEDIA_CURRENT_STATE_LOOP
import com.ssn.sxmusic.util.Const.MEDIA_LOOP_ALL
import com.ssn.sxmusic.util.Const.MEDIA_LOOP_ONE
import com.ssn.sxmusic.util.PrefControllerSingleton
import com.ssn.sxmusic.util.Util

class DetailSong : AppCompatActivity() {
    lateinit var binding: ActivityDetailSongBinding
    private lateinit var seekBar: SeekBar
    private val mBReceiver = SongBReceiver()
    private val sharedPrefControl = PrefControllerSingleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG_LOG", "DetailSong , onCreate")
//
        binding = ActivityDetailSongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onclickItem()
        supportActionBar!!.hide()
        seekBar = binding.seekBar
        setStatusButton(MediaController.mediaState)
        MediaController.currentSong?.let { showInfoSong(it) }
        sharedPrefControl.prefController(this)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("TAG ","onNewIntent Detail {$intent}")
    }




    override fun onStart() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Const.SERVICE_SEND_DATA)
        registerReceiver(mBReceiver, intentFilter)
        Log.d("TAG_LOG", "DetailSong , onStart")
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(mBReceiver)
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("TAG_LOG", "DetailSong , onDestroy")
        super.onDestroy()
    }


    inner class SongBReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, p1: Intent?) {
            Log.d("TAG_LOG", "DetailSong , ${p1?.action}")
            p1?.action?.let {
                handleActionMusic()
            }
        }
    }


    private fun handleActionMusic() {
        MediaController.currentSong?.let { showInfoSong(it) }
        setStatusButton(MediaController.mediaState)
    }


    private fun onclickItem() {
        binding.playMusic.setOnClickListener {
            if (MediaController.mediaState == Const.MEDIA_PLAYING) {
                sendActionToService(ACTION_PAUSE)
            } else {
                sendActionToService(ACTION_PLAYING)
            }
        }

        binding.bntNext.setOnClickListener {
            sendActionToService(ACTION_NEXT)
        }

        binding.bntPrevious.setOnClickListener {
            sendActionToService(ACTION_PREVIOUS)
        }

        binding.repeat.setOnClickListener {
            when (sharedPrefControl.getMediaLoop(MEDIA_CURRENT_STATE_LOOP, MEDIA_LOOP_ALL)) {
                MEDIA_LOOP_ONE -> {
                    binding.repeat.setImageResource(R.drawable.ic_repeat)
                    sharedPrefControl.setMediaLoop(MEDIA_CURRENT_STATE_LOOP, MEDIA_LOOP_ALL)
                }
                MEDIA_LOOP_ALL -> {
                    binding.repeat.setImageResource(R.drawable.ic_repeat_once)
                    sharedPrefControl.setMediaLoop(MEDIA_CURRENT_STATE_LOOP, MEDIA_LOOP_ONE)
                }
            }
        }
        binding.love.setOnClickListener {
            binding.love.setImageResource(R.drawable.ic_favorite_border)
        }
        binding.bntBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showInfoSong(s: Song) {
        binding.nameMusic.text = s.title
        binding.creatorMusic.text = s.creator
        binding.timeMusic.text = Util.formatTime(MediaController.getDuration()!!.toLong())
        Glide.with(binding.imageMusic).load(s.bgImage)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.drawable.defaultsong)
            .into(binding.imageMusic)
        val loop = sharedPrefControl.getMediaLoop(MEDIA_CURRENT_STATE_LOOP, MEDIA_LOOP_ALL)
        if (loop == MEDIA_LOOP_ONE) {
            binding.repeat.setImageResource(R.drawable.ic_repeat_once)
        }
        initialiseSeekbar()
    }


    private fun initialiseSeekbar() {
        seekBar.progress = 0
        seekBar.max = MediaController.getDuration()!!
        onSeekBarChange()
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.timeCurrentMusic.text = Util.formatTime(seekBar.progress.toLong())
                    seekBar.progress = MediaController.getCurrentPosition()!!
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    seekBar.progress = 0
                }
            }
        }, 0)
    }

    private fun onSeekBarChange() {
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MediaController.setPosition(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }


    private fun sendActionToService(action: String) {
        val intentNext = Intent(action)
        sendBroadcast(intentNext)
    }

    private fun setStatusButton(isPlay: Int) {
        if (isPlay == Const.MEDIA_PLAYING) {
            binding.playMusic.setImageResource(R.drawable.custom_ic_pause)
        } else {
            binding.playMusic.setImageResource(R.drawable.custom_ic_play)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
    }

}