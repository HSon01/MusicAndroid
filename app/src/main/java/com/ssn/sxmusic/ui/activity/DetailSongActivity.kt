package com.ssn.sxmusic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.URLUtil
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ssn.sxmusic.R
import com.ssn.sxmusic.databinding.ActivityDetailSongBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.media.MediaController.mediaStateLiveData
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
import com.ssn.sxmusic.vm.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class DetailSongActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailSongBinding
    private lateinit var seekBar: SeekBar
    var stopSeekbar = false
    private val sharedPrefControl = PrefControllerSingleton
    private val musicViewModel: MusicViewModel by viewModels()
    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        this.runOnUiThread(updateUi)
    }

    private fun initView() {
        binding = ActivityDetailSongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefControl.prefController(this) //Loop Song
        supportActionBar!!.hide()
        onclickItem()
        setStatusButton()
    }

    private fun showUI() {
        seekBar = binding.seekBar
        MediaController.currentSong?.let {
            binding.nameMusic.text = it.title
            binding.creatorMusic.text = it.creator
            binding.timeMusic.text = Util.formatTime(MediaController.getDuration()!!.toLong())
            val isLink = URLUtil.isValidUrl(it.bgImage)
            if (!this.isDestroyed) {
                if (!isLink) {
                    binding.imageSong.setImageResource(R.drawable.logo_app_removebg)
                } else {
                    Glide.with(binding.imageSong)
                        .load(it.bgImage)
                        .placeholder(R.drawable.logo_app_removebg)
                        .into(binding.imageSong)
                }
            }
            val loop = sharedPrefControl.getMediaLoop(MEDIA_CURRENT_STATE_LOOP, MEDIA_LOOP_ALL)
            if (loop == MEDIA_LOOP_ONE) {
                binding.repeat.setImageResource(R.drawable.ic_repeat_once)
            }
            lifecycleScope.launch(Dispatchers.Main) {
                if (musicViewModel.findSongByName(it.title)) {
                    binding.love.setImageResource(R.drawable.ic_favorite_border)
                } else {
                    binding.love.setImageResource(R.drawable.ic_favorite)
                }
            }
            seekBar.max = MediaController.getDuration()!!
            onSeekBarChange()
        }

    }


    private val updateUi by lazy {
        object : Runnable {
            override fun run() {
                showUI()
                if (!stopSeekbar) {
                    try {
                        binding.timeCurrentMusic.text = Util.formatTime(seekBar.progress.toLong())
                        seekBar.progress = MediaController.getCurrentPosition()!!
                    } catch (e: Exception) {
                        seekBar.progress = 0
                    }
                }
                handler.postDelayed(this, 500)
            }
        }
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
                    binding.repeat.setImageResource(com.ssn.sxmusic.R.drawable.ic_repeat)
                    sharedPrefControl.setMediaLoop(MEDIA_CURRENT_STATE_LOOP, MEDIA_LOOP_ALL)
                }
                MEDIA_LOOP_ALL -> {
                    binding.repeat.setImageResource(com.ssn.sxmusic.R.drawable.ic_repeat_once)
                    sharedPrefControl.setMediaLoop(MEDIA_CURRENT_STATE_LOOP, MEDIA_LOOP_ONE)
                }
            }
        }

        binding.love.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                MediaController.currentSong?.let {
                    if (musicViewModel.findSongByName(it.title)) {
                        musicViewModel.deleteSong(it.title)
                        withContext(Dispatchers.Main) {
                            binding.love.setImageResource(R.drawable.ic_favorite)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            binding.love.setImageResource(R.drawable.ic_favorite_border)
                        }
                        MediaController.currentSong?.let { it1 -> musicViewModel.addSong(it1) }
                    }
                }

            }
        }
        binding.bntBack.setOnClickListener {
            onBackPressed()
        }
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


    private fun setStatusButton() {
        mediaStateLiveData.observe(this, {
            if (it == Const.MEDIA_PLAYING) {
                binding.playMusic.setImageResource(R.drawable.ic_pause)
                val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.rotate_image)
                binding.imageSong.startAnimation(animation)
            } else {
                binding.playMusic.setImageResource(R.drawable.ic_play)
                binding.imageSong.clearAnimation()
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
        binding.imageSong.clearAnimation()
        stopSeekbar = true
    }

    override fun onStart() {
        stopSeekbar = false
        super.onStart()
    }

    override fun onStop() {
        stopSeekbar = true
        super.onStop()
    }

}