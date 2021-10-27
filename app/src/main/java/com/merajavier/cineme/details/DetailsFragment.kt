package com.merajavier.cineme.details

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.R
import com.merajavier.cineme.cast.ActorsRecyclerAdapter
import com.merajavier.cineme.cast.CastListViewModel
import com.merajavier.cineme.common.toPercentAverage
import com.merajavier.cineme.databinding.FragmentDetailsBinding
import com.merajavier.cineme.genre.GenresRecyclerAdapter
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.login.account.MarkFavoriteRequest
import com.merajavier.cineme.network.NetworkAccountRepositoryInterface
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var genresAdapter: GenresRecyclerAdapter
    private lateinit var actorsAdapter: ActorsRecyclerAdapter
    private val castListViewModel: CastListViewModel by viewModel()
    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val accountRepository: NetworkAccountRepositoryInterface by inject()

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        genresAdapter = GenresRecyclerAdapter()
        actorsAdapter = ActorsRecyclerAdapter()

        binding.detailsMovieGenres.adapter = genresAdapter
        binding.detailsMovieActors.adapter = actorsAdapter

        binding.movie = args.movie
        genresAdapter.submitList(args.movie.genres)
        castListViewModel.getMovieActors(args.movie.id)
        castListViewModel.getDirectors(args.movie.id)

        val percentAverage = args.movie.voteAverage.toPercentAverage()
        val progressBarAnimator = ProgressBarAnimation(
            binding.detailsMovieUserScoreProgress,
            0.0,
            percentAverage
        )
        progressBarAnimator.duration = 2000
        binding.detailsMovieUserScoreProgress.startAnimation(progressBarAnimator)

        val progressPercentAnimator = ValueAnimator.ofInt(0, percentAverage.toInt())
        progressPercentAnimator.duration = 2000
        progressPercentAnimator.addUpdateListener { animation ->
            binding.detailsMovieUserScorePercentage.text = animation.animatedValue.toString()
        }
        progressPercentAnimator.start()

        val listener = AppBarLayout.OnOffsetChangedListener{ unsued, verticalOffset ->
            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        castListViewModel.actors.observe(viewLifecycleOwner, Observer {
            it.let {
                actorsAdapter.submitList(it)
            }
        })

        castListViewModel.crew.observe(viewLifecycleOwner, Observer {
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

        binding.detailsMovieFavorite.setOnClickListener {

            if(loginViewModel.isLogged.value == true){
                lifecycleScope.launch {

                    val response = accountRepository.markFavorite(
                        loginViewModel.userSession.sessionId,
                        MarkFavoriteRequest("movie", args.movie.id, true)
                    )

                    if(response.success){
                        Toast.makeText(
                            requireContext(), "Movie added!.", Toast.LENGTH_SHORT)
                            .show()

                        binding.detailsMovieFavorite.setImageResource(R.drawable.movie_favorite_selected)
                    }
                }
            }else{
                Toast.makeText(
                    requireContext(), "You need to sign in to add to favorites.", Toast.LENGTH_SHORT)
                        .show()
            }
        }
        return binding.root
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