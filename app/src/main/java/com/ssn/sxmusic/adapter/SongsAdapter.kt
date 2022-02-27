package com.ssn.sxmusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssn.sxmusic.adapter.diff.SongsDiff
import com.ssn.sxmusic.databinding.ItemmusicBinding
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.OnClickItem

class SongsAdapter(val onClick: OnClickItem) :
    ListAdapter<Song, SongsAdapter.ViewHolder>(SongsDiff), Filterable {
    private var list: ArrayList<Song> = arrayListOf()

    fun setData(list: ArrayList<Song>) {
        this.list = list
        submitList(list)
    }

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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: ArrayList<Song> = arrayListOf()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(list)
                } else {
                    for (item in list) {
                        if (item.title.toLowerCase()
                                .startsWith(constraint.toString().toLowerCase())
                        ) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as ArrayList<Song>)
            }
        }
    }
}