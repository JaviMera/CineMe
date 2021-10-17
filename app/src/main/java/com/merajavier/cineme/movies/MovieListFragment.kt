package com.merajavier.cineme.movies

import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentMoviesBinding
import com.merajavier.cineme.network.TMDBApi
import retrofit2.Response
import retrofit2.awaitResponse

class MovieListFragment : Fragment() {

    private lateinit var _binding: FragmentMoviesBinding
    private lateinit var _viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _viewModel = MoviesViewModel(requireActivity().application)
        // Inflate the layout for this fragment
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = this

        _viewModel.movie.observe(viewLifecycleOwner, Observer {

            it.let {
                _binding.movieTitle.text = it.originalTitle
            }
        })
        return _binding.root
    }
}