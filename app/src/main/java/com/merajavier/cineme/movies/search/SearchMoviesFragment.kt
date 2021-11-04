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
import com.merajavier.cineme.movies.MoviesFooterAdapter
import com.merajavier.cineme.network.ConnectivityLiveData
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

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

        binding.fragmentSearchRecyclerView.adapter = searchMoviesAdapter.withLoadStateFooter(
            MoviesFooterAdapter()
        )

        searchViewModel.movieSelected.observe(viewLifecycleOwner, Observer {
            it?.let{ movie ->
                findNavController().navigate(SearchMoviesFragmentDirections.actionNavigationSearchMoviesToDetailsFragment(movie))
            }
        })

        val connectivityLiveData = ConnectivityLiveData(requireActivity().application)
        connectivityLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { isConnected ->
                if(isConnected){
                    searchViewModel.queryTitle.observe(viewLifecycleOwner, Observer { text ->
                        text?.let{ title ->
                            searchViewModel.fetchMovies(title).observe(viewLifecycleOwner, Observer {
                                lifecycleScope.launch {
                                    searchMoviesAdapter.submitData(it)
                                }
                            })
                        }
                    })
                }else{
                    searchViewModel.queryTitle.removeObservers(viewLifecycleOwner)
                }
            }
        })

        return binding.root
    }
}

