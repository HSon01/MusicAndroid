package com.ssn.sxmusic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ssn.sxmusic.adapter.AlbumAdapter
import com.ssn.sxmusic.databinding.FragmentGenreBinding
import com.ssn.sxmusic.media.MediaController
import com.ssn.sxmusic.model.SongsX
import com.ssn.sxmusic.util.OnClickAlbum
import com.ssn.sxmusic.vm.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GenreFragment : Fragment(com.ssn.sxmusic.R.layout.fragment_genre), OnClickAlbum {
    private lateinit var binding: FragmentGenreBinding
    private val musicViewModel: MusicViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenreBinding.inflate(inflater, container, false)

        val adapter = setupRecyclerview()
        observerLivedata(adapter)
        return binding.root
    }

    private fun setupRecyclerview(): AlbumAdapter {
        val gridview = GridLayoutManager(this.context, 2)
        val adapter = AlbumAdapter(this)
        binding.rvAlbum.adapter = adapter
        binding.rvAlbum.layoutManager = gridview
        return adapter
    }

    private fun observerLivedata(adapter: AlbumAdapter) {
        musicViewModel.getAlbum()
        musicViewModel.album.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
    }

    override fun onClickListener(x: SongsX) {
        MediaController.setListSong(x.songs)
        val action = GenreFragmentDirections.actionGenreFragmentToDetailAlbumFragment(x.name)
        findNavController().navigate(action)
    }
}