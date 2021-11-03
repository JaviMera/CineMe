package com.merajavier.cineme.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.data.local.NowPlayingMoviesDao
import com.merajavier.cineme.databinding.FragmentMoviesBinding
import com.merajavier.cineme.network.ConnectivityLiveData
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

@ExperimentalPagingApi
class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesRecyclerAdapter

    @ExperimentalPagingApi
    private val viewModel: MovieListViewModel by inject()

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

        viewModel.movieSelected.observe(viewLifecycleOwner, Observer {
            it.let {
                findNavController().navigate(MovieListFragmentDirections.actionNavigationMoviesToDetailsFragment(it))
            }
        })

        val connectivityLiveData = ConnectivityLiveData(requireActivity().application)
        connectivityLiveData.observe(viewLifecycleOwner, Observer {
            it?.let{
                if(it){

                    viewModel.fetchLocalMovies().removeObservers(viewLifecycleOwner)
                    viewModel.fetchMovies().observe(viewLifecycleOwner, Observer { pagingData ->
                        lifecycleScope.launch {
                            binding.recycleViewMovies.scrollToPosition(0)
                            moviesAdapter.submitData(pagingData)
                        }
                    })
                }else{
                    val snackbar: Snackbar = Snackbar
                        .make(binding.recycleViewMovies,
                            "Connection Lost", Snackbar.LENGTH_SHORT)

                    snackbar.show()

                    viewModel.fetchMovies().removeObservers(viewLifecycleOwner)
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