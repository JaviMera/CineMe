package com.merajavier.cineme.movies.rate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.merajavier.cineme.MessageViewModel
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentRateBinding
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.movies.MoviesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalPagingApi
class RateFragment : Fragment() {

    private lateinit var binding: FragmentRateBinding
    private val moviesViewModel: MoviesViewModel by viewModel()
    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val messageViewModel: MessageViewModel by sharedViewModel()

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

        binding.rateMovieButton.setOnClickListener {
            if(loginViewModel.isLogged.value == true){
                if(binding.rateMovieRatingbar.rating < 0.5f){
                    messageViewModel.setMessage("Please choose a rating greater than half a star.")
                }else{
                    moviesViewModel.rate(args.movie.id, binding.rateMovieRatingbar.rating, loginViewModel.userSession.sessionId)
                }
            }else{
                messageViewModel.setMessage(getString(R.string.sign_in_rate_error_message))
            }
        }

        moviesViewModel.isMovieRated.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().popBackStack(R.id.detailsFragment, true)
            }else{
                messageViewModel.setMessage("Unable to rate. Please try again.")
            }
        })

        return binding.root
    }
}