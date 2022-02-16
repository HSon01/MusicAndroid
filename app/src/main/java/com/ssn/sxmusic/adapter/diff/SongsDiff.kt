package com.ssn.sxmusic.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.ssn.sxmusic.model.Song

object SongsDiff : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}