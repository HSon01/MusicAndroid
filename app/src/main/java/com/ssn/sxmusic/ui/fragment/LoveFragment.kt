package com.ssn.sxmusic.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssn.sxmusic.adapter.SongsAdapter
import com.ssn.sxmusic.databinding.FragmentLoveBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.OnClickItem
import com.ssn.sxmusic.vm.MusicViewModel

class LoveFragment : Fragment(), OnClickItem {
    private lateinit var binding: FragmentLoveBinding
    private val musicViewModel: MusicViewModel by viewModels()
    private var musicAdapter: SongsAdapter = SongsAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoveBinding.inflate(inflater, container, false)
        binding.listFavorites.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.listFavorites.adapter = musicAdapter
        musicViewModel.getSongs()
        musicViewModel.listMusic.observe(viewLifecycleOwner, {
            musicAdapter.setData(it)
            MediaController.setListSong(it)
        })
        return  binding.root
    }

    override fun onClickListener(song: Song) {
        val intent = Intent(Const.FRAGMENT_SEND_DATA)
        MediaController.setCurrentSong(MediaController.findSongByPosition(song))
        context?.sendBroadcast(intent)
    }

}