package com.merajavier.cineme.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.RecyclerViewMovieItemBinding

class MoviesRecyclerAdapter(
    private val onMovieClickListener: OnMovieClickListener
) : PagingDataAdapter<MovieDataItem, MoviesRecyclerAdapter.MovieViewHolder>(DiffCallback) {

    class MovieViewHolder(private val binding: RecyclerViewMovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieDataItem: MovieDataItem){
            binding.movie = movieDataItem
        }
    }

    class OnMovieClickListener(val clickListener: (movieId: Int) -> Unit){
        fun onClick(movieId: Int) = clickListener(movieId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.recycler_view_movie_item, parent,false) as RecyclerViewMovieItemBinding
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val movie = getItem(position)
        holder.itemView.setOnClickListener{
            movie?.id?.let { movieId -> onMovieClickListener.onClick(movieId) }
        }

        movie?.let { holder.bind(it) }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MovieDataItem>(){
        override fun areItemsTheSame(oldItem: MovieDataItem, newItem: MovieDataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDataItem, newItem: MovieDataItem): Boolean {
            return oldItem == newItem
        }
    }
}

