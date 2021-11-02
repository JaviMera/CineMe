package com.merajavier.cineme.movies.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.paging.ExperimentalPagingApi
import com.merajavier.cineme.CinemaActivity
import com.merajavier.cineme.databinding.FragmentSearchMoviesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchMoviesFragment : Fragment() {

    private lateinit var binding: FragmentSearchMoviesBinding

    @ExperimentalPagingApi
    private val searchViewModel: SearchMoviesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchMoviesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    fun searchMovies(text: CharSequence?) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}