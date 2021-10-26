package com.merajavier.cineme.details

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.merajavier.cineme.cast.CastListViewModel
import com.merajavier.cineme.cast.ActorsRecyclerAdapter
import com.merajavier.cineme.common.toPercentAverage
import com.merajavier.cineme.databinding.ActivityDetailsBinding
import com.merajavier.cineme.genre.GenresRecyclerAdapter
import com.merajavier.cineme.movies.MovieDataItem
import org.koin.android.ext.android.inject

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var genresAdapter: GenresRecyclerAdapter
    private lateinit var actorsAdapter: ActorsRecyclerAdapter
    private val castListViewModel: CastListViewModel by inject()

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
                castListViewModel.getMovieActors(it.id)
                castListViewModel.getDirectors(it.id)

                val progressBarAnimator = ProgressBarAnimation(binding.detailsMovieUserScoreProgress, 0.0, it.voteAverage.toPercentAverage())
                progressBarAnimator.duration = 2000
                binding.detailsMovieUserScoreProgress.startAnimation(progressBarAnimator)

                val progressPercentAnimator = ValueAnimator.ofInt(0, it.voteAverage.toPercentAverage().toInt())
                progressPercentAnimator.duration = 2000
                progressPercentAnimator.addUpdateListener { animation ->
                    binding.detailsMovieUserScorePercentage.text = animation.animatedValue.toString()
                }

                progressPercentAnimator.start()
            }
        }

        intent?.getStringExtra("SESSION_ID").let { sessionId ->
            binding.detailsMovieFavorite.setOnClickListener {

                Toast.makeText(this, sessionId.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        val listener = AppBarLayout.OnOffsetChangedListener{unsued, verticalOffset ->
            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        castListViewModel.actors.observe(this, Observer {
            it.let {
                actorsAdapter.submitList(it)
            }
        })

        castListViewModel.crew.observe(this, Observer {
            it.let { crew ->
                val directors = crew
                    .filter { member -> member.job == "Director" }
                    .map { member -> member.name}
                    .joinToString()

                binding.detailsMovieDirector.text = directors

                val writers = crew
                    .filter { member -> member.department == "Writing" }
                    .joinToString { member -> "${member.name} (${member.job})" }

                binding.detailsMovieWriters.text = writers
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