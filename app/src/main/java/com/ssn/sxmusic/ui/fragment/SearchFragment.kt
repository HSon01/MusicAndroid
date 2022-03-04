package com.ssn.sxmusic.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssn.sxmusic.adapter.SongsAdapter
import com.ssn.sxmusic.databinding.FragmentListmusicBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.OnClickItem
import com.ssn.sxmusic.vm.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), OnClickItem {
    private lateinit var binding: FragmentListmusicBinding
    private var musicAdapter: SongsAdapter = SongsAdapter(this)
    private val musicViewModel: MusicViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListmusicBinding.inflate(inflater, container, false)
        setupRecyclerview()
        observerLivedata()
        return binding.root
    }

    private fun observerLivedata() {
        musicViewModel.listMusic.observe(viewLifecycleOwner, {
            musicAdapter.setData(it)
            MediaController.setListSong(it)
        })
    }

    private fun setupRecyclerview() {
        binding.listMusic.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.listMusic.adapter = musicAdapter
        searchMusic()
    }

    override fun onClickListener(Song: Song) {
        MediaController.setCurrentSong(MediaController.findSongByPosition(Song))
        val intent = Intent(Const.FRAGMENT_SEND_DATA)
        requireActivity().sendBroadcast(intent)
    }


    private fun searchMusic() {
        binding.searchMusic.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                musicAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                musicAdapter.filter.filter(newText)
                return false
            }

        })
    }
}

