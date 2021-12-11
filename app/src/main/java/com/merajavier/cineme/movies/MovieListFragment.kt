package com.merajavier.cineme.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.merajavier.cineme.databinding.FragmentMoviesBinding
import com.merajavier.cineme.network.ConnectivityLiveData
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@ExperimentalPagingApi
class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesRecyclerAdapter

    @ExperimentalPagingApi
    private val viewModel: MoviesViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        moviesAdapter = MoviesRecyclerAdapter(MoviesRecyclerAdapter.OnMovieClickListener{
            viewModel.getMovieDetails(it)
        })

        binding.recycleViewMovies.adapter = moviesAdapter.withLoadStateFooter(MoviesFooterAdapter())

        val connectivityLiveData = ConnectivityLiveData(requireActivity().application)
        connectivityLiveData.observe(viewLifecycleOwner, Observer {
            it?.let{ isConnected ->
                if(isConnected){

                    // Remove observer that pulls data from the database
                    viewModel.fetchLocalMovies().removeObservers(viewLifecycleOwner)

                    // Add any observers from live data objects that deal with network calls
                    viewModel.fetchMovies().observe(viewLifecycleOwner, Observer { pagingData ->
                        lifecycleScope.launch {
                            binding.recycleViewMovies.scrollToPosition(0)
                            moviesAdapter.submitData(pagingData)
                        }

                    })
                    viewModel.movieSelected.observe(viewLifecycleOwner, Observer {
                        it?.let { movie ->
                            findNavController().navigate(MovieListFragmentDirections.actionNavigationMoviesToDetailsFragment(movie))
                        }
                    })
                }else{

                    // Remove any observers from live data objects that deal with network calls
                    viewModel.fetchMovies().removeObservers(viewLifecycleOwner)
                    viewModel.movieSelected.removeObservers(viewLifecycleOwner)

                    // Add observer that pulls data from the database
                    viewModel.fetchLocalMovies().observe(viewLifecycleOwner, Observer {
                        lifecycleScope.launch {
                            binding.recycleViewMovies.scrollToPosition(0)
                            moviesAdapter.submitData(it)
                        }
                    })
                }
            }
        })
        return binding.root
    }
}