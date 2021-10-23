package com.merajavier.cineme.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.merajavier.cineme.cast.ActorListViewModel
import com.merajavier.cineme.cast.ActorsRecyclerAdapter
import com.merajavier.cineme.databinding.ActivityDetailsBinding
import com.merajavier.cineme.genre.GenresRecyclerAdapter
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.network.NetworkMovieActorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var genresAdapter: GenresRecyclerAdapter
    private lateinit var actorsAdapter: ActorsRecyclerAdapter
    private val actorListViewModel: ActorListViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        genresAdapter = GenresRecyclerAdapter()
        actorsAdapter = ActorsRecyclerAdapter()

        binding.detailsMovieGenres.adapter = genresAdapter
        binding.detailsMovieActors.adapter = actorsAdapter

        intent?.getParcelableExtra<MovieDataItem>(SELECTED_MOVIE_ID).let {

            if(it != null){
                binding.movie = it
                genresAdapter.submitList(it.genres)
                actorListViewModel.getMovieActors(it.id)
            }
        }

        val listener = AppBarLayout.OnOffsetChangedListener{unsued, verticalOffset ->
            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        actorListViewModel.actors.observe(this, Observer {
            it.let {
                actorsAdapter.submitList(it)
            }
        })

        binding.appbarLayout.addOnOffsetChangedListener(listener)
    }

    companion object{
        const val SELECTED_MOVIE_ID = "SELECTED_MOVIE_ID"
    }
}