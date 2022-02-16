package com.ssn.sxmusic.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.ssn.sxmusic.model.SongsX

object AlbumsDiff : DiffUtil.ItemCallback<SongsX>() {
    override fun areItemsTheSame(oldItem: SongsX, newItem: SongsX): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: SongsX, newItem: SongsX): Boolean {
        return oldItem == newItem
    }
}