package com.ssn.sxmusic.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.ssn.sxmusic.model.Song

object SearchDiff : DiffUtil.ItemCallback<Song>() {

    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.music == newItem.music
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }


}