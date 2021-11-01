package com.merajavier.cineme.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.databinding.FragmentMoviesBinding
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

        binding.recycleViewMovies.adapter = moviesAdapter

        viewModel.fetchMovies().observe(viewLifecycleOwner, Observer { pagingData ->
            lifecycleScope.launch {
                moviesAdapter.submitData(pagingData)
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it.let {
                when(it){
                    true -> {
                        binding.loadingIndicator.visibility = View.VISIBLE
                    }
                    false -> {
                        binding.loadingIndicator.visibility = View.GONE
                    }
                }
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