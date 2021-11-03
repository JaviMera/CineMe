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
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

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

//        viewModel.fetchMovies().observe(viewLifecycleOwner, Observer { pagingData ->
//            lifecycleScope.launch {
//                moviesAdapter.submitData(pagingData)
//            }
//        })

        viewModel.fetchMoviesWithDb().observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                moviesAdapter.submitData(it)
            }
        })

        viewModel.movieSelected.observe(viewLifecycleOwner, Observer {
            it.let {
                findNavController().navigate(MovieListFragmentDirections.actionNavigationMoviesToDetailsFragment(it))
            }
        })

        return binding.root
    }
}