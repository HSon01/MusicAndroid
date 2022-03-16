package com.ssn.sxmusic.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssn.sxmusic.adapter.diff.SearchDiff
import com.ssn.sxmusic.databinding.ItemmusicBinding
import com.ssn.sxmusic.model.Song
import com.ssn.sxmusic.util.OnClickItem
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(val onClick: OnClickItem) :
    ListAdapter<Song, SearchAdapter.ViewHolder>(SearchDiff), Filterable {
    private var songs: ArrayList<Song>? = null


    fun setData(list: ArrayList<Song>) {
        this.songs = list
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

    override fun getFilter(): Filter {
        return object : Filter() {


            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.d("AVG", "1 performFiltering")
                val filterResults = FilterResults()
                val filterString = constraint.toString().lowercase(Locale.getDefault()).trim()

                val count = songs!!.size
                val nlist = ArrayList<Song>(count)

                songs!!.forEach {
                    if (it.title.lowercase(Locale.getDefault()).trim()
                            .startsWith(filterString)
                    ) {
                        nlist.add(it)
                    }
                }
                filterResults.count = nlist.size
                filterResults.values = nlist
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.d("AVG", "2 publishResults")
                if (results?.values != null) {
                    submitList(results.values as ArrayList<Song>)
                } else {
                    submitList(songs)
                }
            }
        }
    }

}