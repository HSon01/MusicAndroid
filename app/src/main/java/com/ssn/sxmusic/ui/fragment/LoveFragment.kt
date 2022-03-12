package com.ssn.sxmusic.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssn.sxmusic.adapter.FavoritesAdapter
import com.ssn.sxmusic.databinding.FragmentLoveBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.OnClickItem
import com.ssn.sxmusic.util.OnDeleteItem
import com.ssn.sxmusic.vm.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoveFragment : Fragment(), OnClickItem, OnDeleteItem {
    private lateinit var binding: FragmentLoveBinding
    private val musicViewModel: MusicViewModel by viewModels()
    private var musicAdapter: FavoritesAdapter = FavoritesAdapter(this, this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoveBinding.inflate(inflater, container, false)
        observerLivedata()
        setupRecyclerview()
        return binding.root
    }

    private fun observerLivedata() {
        musicViewModel.getFavoriteSongs().observe(viewLifecycleOwner, {
            musicAdapter.submitList(it)
            MediaController.setListSong(it)
        })
    }

    private fun setupRecyclerview() {
        binding.listFavorites.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.listFavorites.adapter = musicAdapter
    }


    override fun onStart() {
        observerLivedata()
        super.onStart()
    }


    override fun onClickListener(Song: Song) {
        val intent = Intent(Const.FRAGMENT_SEND_DATA)
        val result = MediaController.findSongByPosition(Song)
        if( result == -1){
            MediaController.currentSong = Song
        }else{
            MediaController.setCurrentSong(MediaController.findSongByPosition(Song))
        }
        context?.sendBroadcast(intent)
    }

    override fun onDeleteItemListener(name: String) {
        Log.d("TAG delete", name)
        lifecycleScope.launch {
            musicViewModel.deleteSong(name)
        }
    }

}