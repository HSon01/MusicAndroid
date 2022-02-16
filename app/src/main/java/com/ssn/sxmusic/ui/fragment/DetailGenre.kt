package com.ssn.sxmusic.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssn.sxmusic.adapter.SongsAdapter
import com.ssn.sxmusic.databinding.FragmentDetailGenreBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.Const
import com.ssn.sxmusic.util.OnClickItem

class DetailGenre : Fragment(), OnClickItem {
    private lateinit var binding: FragmentDetailGenreBinding
    private val args: DetailGenreArgs by navArgs()
    private var musicAdapter: SongsAdapter = SongsAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailGenreBinding.inflate(inflater, container, false)

        binding.listMusic.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.listMusic.adapter = musicAdapter
        binding.title.text = args.genre
        musicAdapter.setData(MediaController.songs)
        onClick()
        return binding.root
    }

    override fun onClickListener(song: Song) {
        val intent = Intent(Const.FILTER_SEND_DATA)
        MediaController.setCurrentSong(MediaController.findSongByPosition(song))
        context?.sendBroadcast(intent)
    }

    fun onClick(){
        binding.bntBack.setOnClickListener {
            val action = DetailGenreDirections.actionDetailAlbumFragmentToGenreFragment()
            findNavController().navigate(action)
            val manager: FragmentManager = requireActivity().supportFragmentManager
            val trans: FragmentTransaction = manager.beginTransaction()
            trans.remove(requireParentFragment());
            trans.commit();
            manager.popBackStack();
        }
    }


}