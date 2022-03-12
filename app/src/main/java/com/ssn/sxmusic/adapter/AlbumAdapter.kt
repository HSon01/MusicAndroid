package com.ssn.sxmusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssn.sxmusic.adapter.diff.AlbumsDiff
import com.ssn.sxmusic.databinding.ItemalbumBinding
import com.ssn.sxmusic.model.SongsX
import com.ssn.sxmusic.util.OnClickAlbum

class AlbumAdapter(val onClick: OnClickAlbum) :
    ListAdapter<SongsX, AlbumAdapter.ViewHolder>(AlbumsDiff) {
    private var list: ArrayList<SongsX> = arrayListOf()

    fun setData(list: ArrayList<SongsX>) {
        this.list = list
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapter.ViewHolder {
        return ViewHolder(
            ItemalbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumAdapter.ViewHolder, position: Int) {
        var item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(private var binding: ItemalbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(x: SongsX) {
            binding.tvTitleGenre.text = x.name
            binding.root.setOnClickListener {
                onClick.onClickAlbumListener(x)
            }
        }
    }
}