package com.merajavier.cineme.movies.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.databinding.FragmentSearchMoviesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalPagingApi
class SearchMoviesFragment : Fragment() {

    private lateinit var binding: FragmentSearchMoviesBinding
    private lateinit var searchMoviesAdapter: SearchMoviesAdapter

    @ExperimentalPagingApi
    private val searchViewModel: SearchMoviesViewModel by sharedViewModel()

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

        searchMoviesAdapter = SearchMoviesAdapter(SearchMoviesAdapter.OnMovieClickListener {
            searchViewModel.getMovieDetails(it)
        })

        binding.fragmentSearchRecyclerView.adapter = searchMoviesAdapter.withLoadStateFooter(SearchMoviesFooterAdapter())

        searchViewModel.movieSelected.observe(viewLifecycleOwner, Observer {
            it?.let{ movie ->
                findNavController().navigate(SearchMoviesFragmentDirections.actionNavigationSearchMoviesToDetailsFragment(movie))
            }
        })

        searchViewModel.queryTitle.observe(viewLifecycleOwner, Observer {
            it?.let{ title ->
                searchViewModel.fetchMovies(title).observe(viewLifecycleOwner, Observer {
                    lifecycleScope.launch {
                        searchMoviesAdapter.submitData(it)
                    }
                })
            }
        })

        return binding.root
    }
}

