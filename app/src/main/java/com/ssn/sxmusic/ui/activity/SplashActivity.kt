package com.ssn.sxmusic.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ssn.sxmusic.databinding.ActivitySplashBinding
import com.ssn.sxmusic.vm.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    //    private val musicViewModel: MusicViewModel by viewModels{
//        MusicViewModelFactory(application)
//    }
    private val musicViewModel: MusicViewModel by viewModels()
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        startHomeActivity()
    }

    private fun startHomeActivity() {
        lifecycleScope.launch {
            musicViewModel.getAllMusic()
            withContext(Dispatchers.Main) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                delay(1000L)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {} // Not allowed to operate
}
