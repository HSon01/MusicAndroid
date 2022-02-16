package com.ssn.sxmusic.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.ssn.sxmusic.R
import com.ssn.sxmusic.databinding.ActivityHomeBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.service.MusicService
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.Const.ACTION_PLAYING
import com.ssn.sxmusic.util.Const.MEDIA_PLAYING
import com.ssn.sxmusic.util.Const.SERVICE_SEND_DATA


class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private var mSong: Song? = null
    private val mBReceiver = SongBReceiver()

    inner class SongBReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, p1: Intent?) {
            Log.d("TAG_LOG", "Home , ${p1?.action}")
            p1?.action?.let { handleActionMusic(it) }
        }
    }


    override fun onDestroy() {
        unregisterReceiver(mBReceiver)
        super.onDestroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerReceiver()
        settingView()
        onclickItem()
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(SERVICE_SEND_DATA)
        registerReceiver(mBReceiver, intentFilter)
    }


    private fun settingView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        setupActionBarWithNavController(navController)
        binding.navigationBottom.setupWithNavController(navController)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.navigation_bottom)
        val chipNavigationBar: ChipNavigationBar = findViewById(R.id.navigation_ChipNavigation)
        chipNavigationBar.setItemSelected(R.id.musicsFragment)
        chipNavigationBar.setOnItemSelectedListener { itemId ->
            bottomNavigationView.selectedItemId = itemId
            NavigationUI.setupWithNavController(bottomNavigationView, navController)
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
        if (isPlay == MEDIA_PLAYING) {
            binding.playBtn.setImageResource(R.drawable.ic_pause)
        } else {
            binding.playBtn.setImageResource(R.drawable.ic_play)
        }

    }

    private fun onclickItem() {
        binding.playBtn.setOnClickListener {
            if (MediaController.mediaState == MEDIA_PLAYING) {
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
            val int = Intent(this, DetailSong::class.java)
            startActivity(int)
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
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }


//    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
//        val manager = this.getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager
//        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
//            if (serviceClass.name == service.service.className) {
//                return true
//            }
//        }
//        return false
//    }

}


