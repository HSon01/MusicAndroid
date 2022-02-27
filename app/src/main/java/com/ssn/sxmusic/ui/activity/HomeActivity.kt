package com.ssn.sxmusic.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.ssn.sxmusic.R
import com.ssn.sxmusic.databinding.ActivityHomeBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.service.MusicService
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.Const.ACTION_PLAYING
import com.ssn.sxmusic.util.Const.SERVICE_SEND_DATA
import dagger.hilt.android.AndroidEntryPoint
import com.ssn.sxmusic.util.Const.MEDIA_PLAYING as MEDIA_PLAYING1

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private val mBReceiver = SongBReceiver()

    inner class SongBReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, p1: Intent?) {
            p1?.action?.let { handleActionMusic(it) }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerReceiver()
        settingView()
        onclickItem()
    }

    override fun onDestroy() {
        unregisterReceiver(mBReceiver)
        super.onDestroy()
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(SERVICE_SEND_DATA)
        registerReceiver(mBReceiver, intentFilter)
    }


    override fun onStart() {
        if (MediaController.mediaState == MEDIA_PLAYING1) {
            showUi()
            binding.menuPlay.visibility = View.VISIBLE
        }
        super.onStart()
    }


    private fun settingView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        setupActionBarWithNavController(navController)
        val chipNavigationBar: ChipNavigationBar = findViewById(R.id.navigation_ChipNavigation)
        chipNavigationBar.setItemSelected(R.id.homeFragment)
        chipNavigationBar.setOnItemSelectedListener { itemId ->
            navController.navigate(itemId)
        }
        supportActionBar!!.hide()
        startService()
    }

    private fun startService() {
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
    }


    private fun handleActionMusic(Action: String) {
        setStatusButton(MediaController.mediaState)
        when (Action) {
            SERVICE_SEND_DATA -> {
                showUi()
                binding.menuPlay.visibility = View.VISIBLE
                return
            }
        }
    }


    private fun setStatusButton(isPlay: Int) {
        if (isPlay == MEDIA_PLAYING1) {
            binding.playBtn.setImageResource(R.drawable.ic_pause)
        } else {
            binding.playBtn.setImageResource(R.drawable.ic_play)
        }

    }

    private fun onclickItem() {
        binding.playBtn.setOnClickListener {
            if (MediaController.mediaState == MEDIA_PLAYING1) {
                sendActionToService(Const.ACTION_PAUSE)
            } else {
                sendActionToService(ACTION_PLAYING)
            }
        }

        binding.nextBtn.setOnClickListener {
            sendActionToService(Const.ACTION_NEXT)
        }

        binding.previousBtn.setOnClickListener {
            sendActionToService(Const.ACTION_PREVIOUS)
        }

        binding.menuPlay.setOnClickListener {
            val int = Intent(this, DetailSongActivity::class.java)
            startActivity(int)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
    }


    private fun sendActionToService(action: String) {
        val intentNext = Intent(action)
        sendBroadcast(intentNext)
    }

    private fun showUi() {
        val s: Song? = MediaController.currentSong
        binding.nameSong.text = s?.title
        binding.creatorSong.text = s?.creator
        Glide.with(binding.imageSong).load(s?.bgImage)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.drawable.defaultsong)
            .into(binding.imageSong)
        setStatusButton(MediaController.mediaState)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}

