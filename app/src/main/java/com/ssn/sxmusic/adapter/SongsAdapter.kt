package com.ssn.sxmusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssn.sxmusic.adapter.diff.SongsDiff
import com.ssn.sxmusic.databinding.ItemmusicBinding
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.OnClickItem

class SongsAdapter(val onClick: OnClickItem) :
    ListAdapter<Song, SongsAdapter.ViewHolder>(SongsDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsAdapter.ViewHolder {
        return ViewHolder(
            ItemmusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SongsAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(private var binding: ItemmusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.song = song
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onClick.onClickListener(song)
            }
        }
    }
}