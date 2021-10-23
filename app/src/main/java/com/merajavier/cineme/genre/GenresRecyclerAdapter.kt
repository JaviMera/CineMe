package com.merajavier.cineme.genre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.DetailsMovieGenreChipBinding

class GenresRecyclerAdapter
    : ListAdapter<GenreDataItem, GenresRecyclerAdapter.GenreViewHolder>(DiffCallback){

    class GenreViewHolder(private val binding: DetailsMovieGenreChipBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(genreDataItem: GenreDataItem){
            binding.genre = genreDataItem
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<GenreDataItem>() {

        override fun areItemsTheSame(oldItem: GenreDataItem, newItem: GenreDataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GenreDataItem, newItem: GenreDataItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.details_movie_genre_chip, parent, false) as DetailsMovieGenreChipBinding
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        var genre = getItem(position)
        holder.bind(genre)
    }
}