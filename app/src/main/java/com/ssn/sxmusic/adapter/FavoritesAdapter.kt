package com.ssn.sxmusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssn.sxmusic.adapter.diff.SongsDiff
import com.ssn.sxmusic.databinding.ItemFavoritesBinding
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.OnClickItem
import com.ssn.sxmusic.util.OnDeleteItem

class FavoritesAdapter(val onClick: OnClickItem, val onDelete: OnDeleteItem) :
    ListAdapter<Song, FavoritesAdapter.ViewHolder>(SongsDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.ViewHolder {
        return ViewHolder(
            ItemFavoritesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    inner class ViewHolder(private var binding: ItemFavoritesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.song = song
            binding.executePendingBindings()

            binding.layoutFavorites.setOnClickListener {
                onClick.onClickListener(song)
            }

            binding.layoutDelete.setOnClickListener {
                onDelete.onDeleteItemListener(song.title)
            }
        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }
}