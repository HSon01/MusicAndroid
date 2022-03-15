package com.ssn.sxmusic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.URLUtil
import androidx.activity.viewModels
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
import com.ssn.sxmusic.service.MusicService
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.Const.ACTION_PLAYING
import com.ssn.sxmusic.vm.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.ssn.sxmusic.util.Const.MEDIA_PLAYING as MEDIA_PLAYING1

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val musicViewModel: MusicViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private val handler = Handler()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        this.runOnUiThread(updateUi)
    }

    private fun initView(){
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingView()
        onclickItem()
        setStatusButton()
    }

    private val updateUi by lazy {
        object : Runnable {
            override fun run() {
                showUi()
                handler.postDelayed(this, 500)
            }
        }
    }


    override fun onStart() {
        if (MediaController.mediaState == MEDIA_PLAYING1) {
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


    private fun setStatusButton() {
        MediaController.mediaStateLiveData.observe(this, {
            if (it == Const.MEDIA_PLAYING) {
                binding.playBtn.setImageResource(R.drawable.ic_pause)
            } else {
                binding.playBtn.setImageResource(R.drawable.ic_play)
            }
        })
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
        if (MediaController.mediaState == MEDIA_PLAYING1) {
            binding.menuPlay.visibility = View.VISIBLE
            MediaController.currentSong?.let{
                binding.nameSong.text = it.title
                binding.creatorSong.text = it.creator
                val isLink = URLUtil.isValidUrl(it.bgImage)
                if (!this.isDestroyed) {
                    if(!isLink){
                        binding.imageSong.setImageResource(R.drawable.logo_app_removebg)
                    }else{
                        Glide.with(binding.imageSong)
                            .load(it.bgImage)
                            .placeholder(R.drawable.ic_album)
                            .into(binding.imageSong)
                    }
                }
            }
        }
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

