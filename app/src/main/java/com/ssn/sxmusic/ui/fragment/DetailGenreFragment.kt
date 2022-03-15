package com.ssn.sxmusic.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssn.sxmusic.adapter.SongsAdapter
import com.ssn.sxmusic.databinding.FragmentDetailGenreBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.OnClickItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailGenreFragment : Fragment(), OnClickItem {
    private lateinit var binding: FragmentDetailGenreBinding
    private val args: DetailGenreFragmentArgs by navArgs()
    private var musicAdapter: SongsAdapter = SongsAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailGenreBinding.inflate(inflater, container, false)
        setupUI()
        onClick()
        return binding.root
    }

    private fun setupUI() {
        binding.listMusic.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.listMusic.adapter = musicAdapter
        binding.title.text = args.genre
        lifecycleScope.launch(Dispatchers.IO){
            with(musicAdapter) { setData(MediaController.songs) }
        }
    }

    override fun onClickListener(Song: Song) {
        val intent = Intent(Const.FRAGMENT_SEND_DATA)
        MediaController.setCurrentSong(MediaController.findSongByPosition(Song))
        context?.sendBroadcast(intent)
    }

    private fun onClick() {
        binding.bntBack.setOnClickListener {
            val action = DetailGenreFragmentDirections.actionDetailAlbumFragmentToGenreFragment()
            findNavController().navigate(action)
        }
    }
}