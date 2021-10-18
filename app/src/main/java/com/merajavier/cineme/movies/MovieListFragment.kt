package com.merajavier.cineme.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.merajavier.cineme.databinding.FragmentMoviesBinding
import org.koin.android.ext.android.inject

class MovieListFragment : Fragment() {

    private lateinit var _binding: FragmentMoviesBinding
    private val _viewModel: MoviesViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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