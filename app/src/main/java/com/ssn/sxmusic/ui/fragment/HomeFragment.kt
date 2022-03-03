package com.ssn.sxmusic.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssn.sxmusic.adapter.SongsAdapter
import com.ssn.sxmusic.databinding.FragmentHomeBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.OnClickItem
import com.ssn.sxmusic.vm.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnClickItem {
    private lateinit var binding: FragmentHomeBinding
    private val musicViewModel: MusicViewModel by viewModels()
    private var musicAdapter: SongsAdapter = SongsAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerview()
        observeLiveData()
        return binding.root
    }

    private fun setupRecyclerview() {
        binding.listSong.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.listSong.adapter = musicAdapter
    }

    private fun observeLiveData() {
        musicViewModel.listMusic.observe(viewLifecycleOwner, {
            Log.d("AGT onStart", "CALL observeLiveData")
            musicAdapter.setData(it)
            MediaController.setListSong(it)
        })
    }

    override fun onStart() {
        Log.d("AGT onStart", "CALL onStart")
        musicViewModel.getSongs()
        super.onStart()
    }

    override fun onClickListener(Song: Song) {
        val intent = Intent(Const.FRAGMENT_SEND_DATA)
        MediaController.setCurrentSong(MediaController.findSongByPosition(Song))
        context?.sendBroadcast(intent)
    }

}