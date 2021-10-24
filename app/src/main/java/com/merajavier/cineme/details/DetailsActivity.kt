package com.merajavier.cineme.details

import android.animation.Animator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.merajavier.cineme.cast.ActorListViewModel
import com.merajavier.cineme.cast.ActorsRecyclerAdapter
import com.merajavier.cineme.common.toPercentAverage
import com.merajavier.cineme.databinding.ActivityDetailsBinding
import com.merajavier.cineme.genre.GenresRecyclerAdapter
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.network.NetworkMovieActorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

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

                val animator = ProgressBarAnimation(binding.detailsMovieUserScoreProgress, 0.0, it.voteAverage.toPercentAverage())
                animator.duration = 1000
                binding.detailsMovieUserScoreProgress.startAnimation(animator)
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

    class ProgressBarAnimation(
        private val progressBar: CircularProgressIndicator,
        private val from: Double,
        private val to: Double
    ) : Animation() {

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            super.applyTransformation(interpolatedTime, t)
            val value = from + (to - from) * interpolatedTime
            progressBar.progress = value.toInt()
        }
    }
}