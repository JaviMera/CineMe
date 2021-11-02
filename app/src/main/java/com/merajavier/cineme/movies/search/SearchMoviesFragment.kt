package com.merajavier.cineme.movies.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.databinding.FragmentSearchMoviesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalPagingApi
class SearchMoviesFragment : Fragment() {

    private lateinit var binding: FragmentSearchMoviesBinding
    private lateinit var searchMoviesAdapter: SearchMoviesAdapter

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
        binding.viewModel = searchViewModel

        searchMoviesAdapter = SearchMoviesAdapter()
        binding.fragmentSearchRecyclerView.adapter = searchMoviesAdapter.withLoadStateFooter(SearchMoviesFooterAdapter())
        return binding.root
    }

    fun searchMovies(text: CharSequence?) {
        searchViewModel.fetchMovies(text.toString()).observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                searchMoviesAdapter.submitData(it)
            }
        })
    }
}

