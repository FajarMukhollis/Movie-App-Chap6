package com.fajar.movieapp.presentation.ui.movie.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fajar.movieapp.data.network.model.HomeMovieItem
import com.fajar.movieapp.databinding.ItemPosterBinding

class HomePosterAdapter : RecyclerView.Adapter<HomePosterAdapter.HomePosterViewHolder>() {

    var itemClickListener: ((item: HomeMovieItem) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<HomeMovieItem>() {
        override fun areItemsTheSame(oldItem: HomeMovieItem, newItem: HomeMovieItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeMovieItem, newItem: HomeMovieItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(movie: List<HomeMovieItem>?) {
        differ.submitList(movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePosterViewHolder {
        val binding = ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomePosterViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class HomePosterViewHolder(private val binding: ItemPosterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeMovieItem) {
            with(binding) {
                Glide.with(itemView)
                    .load(IMAGE_URL + item.posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivPoster)

                itemView.setOnClickListener {
                    itemClickListener?.invoke(item)
                }
            }
        }
    }

    companion object {
        private const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
    }
}