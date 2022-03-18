package com.ssn.sxmusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssn.sxmusic.adapter.diff.SearchDiff
import com.ssn.sxmusic.databinding.ItemmusicBinding
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.OnClickItem
//, Filterable
class SearchAdapter(val onClick: OnClickItem) :
    ListAdapter<Song, SearchAdapter.ViewHolder>(SearchDiff) {




    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        return ViewHolder(
            ItemmusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
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