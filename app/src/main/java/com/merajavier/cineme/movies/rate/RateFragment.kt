package com.merajavier.cineme.movies.rate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.account.guest.GuestViewModel
import com.merajavier.cineme.databinding.FragmentRateBinding
import com.merajavier.cineme.genre.RateMovieRequest
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MovieListViewModel
import com.merajavier.cineme.network.NetworkGuestRepositoryInterface
import com.merajavier.cineme.network.NetworkMovieRepository
import com.merajavier.cineme.network.NetworkMovieRepositoryInterface
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class RateFragment : Fragment() {

    private lateinit var binding: FragmentRateBinding
    private val moviesViewModel: MovieListViewModel by viewModel()
    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val guestRepositoryInterface: NetworkMovieRepository by inject()
    private val args: RateFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRateBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.movie = args.movie

        binding.rateMovieButton.setOnClickListener{

            if(binding.rateMovieRatingbar.progress == 0){
                Snackbar.make(
                    binding.rateConstraintLayout,
                    "Please select a rating.",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }else{

                if(loginViewModel.sessionId.isEmpty()){
                    Snackbar.make(
                        binding.rateConstraintLayout,
                        "Sign in as Guest again.",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }else{
                    lifecycleScope.launch {
                        val response = guestRepositoryInterface.rateMovieAsGuest(
                            args.movie.id,
                            loginViewModel.sessionId,
                            RateMovieRequest(binding.rateMovieRatingbar.rating.toDouble())
                        )

                        Timber.i("Rated: $response.statusMessage")

                        requireActivity().supportFragmentManager.setFragmentResult(
                            "MOVIE_RATE",
                            bundleOf("MOVIE_RATE_VALUE" to binding.rateMovieRatingbar.rating.toString()
                            )
                        )

                        findNavController().popBackStack()
                    }
//                    moviesViewModel.rateMovieAsGuest(
//                        args.movie.id,
//                        loginViewModel.sessionId,
//                        binding.rateMovieRatingbar.rating.toDouble())
                }
            }
        }

        moviesViewModel.ratedMovie.observe(viewLifecycleOwner, Observer {
            it.let {
                if(it == true){
                    requireActivity().supportFragmentManager.setFragmentResult(
                        "MOVIE_RATE",
                        bundleOf("MOVIE_RATE_VALUE" to binding.rateMovieRatingbar.rating.toString()
                        )
                    )

                    findNavController().popBackStack()
                }else{
                    Snackbar.make(
                        binding.rateConstraintLayout,
                        "Unable to rate. Try again.",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            }
        })
        return binding.root
    }
}