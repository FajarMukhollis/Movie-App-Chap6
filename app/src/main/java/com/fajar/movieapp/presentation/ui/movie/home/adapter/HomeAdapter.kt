package com.fajar.movieapp.presentation.ui.movie.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fajar.movieapp.data.network.model.HomeMovie
import com.fajar.movieapp.data.network.model.HomeMovieItem
import com.fajar.movieapp.databinding.ItemTitleBinding

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var itemClickListener: ((item: HomeMovieItem) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<HomeMovie>() {
        override fun areItemsTheSame(oldItem: HomeMovie, newItem: HomeMovie): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HomeMovie, newItem: HomeMovie): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(movie: List<HomeMovie>?) {
        differ.submitList(movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    inner class HomeViewHolder(private val binding: ItemTitleBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeMovie) {
            with(binding) {
                tvTitle.text = item.title

                val posterAdapter = HomePosterAdapter()
                posterAdapter.submitList(item.results)
                posterAdapter.itemClickListener = itemClickListener

                rvPoster.adapter = posterAdapter
                rvPoster.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }
}