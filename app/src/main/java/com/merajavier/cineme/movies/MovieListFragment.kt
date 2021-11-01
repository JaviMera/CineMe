package com.merajavier.cineme.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.merajavier.cineme.databinding.FragmentMoviesBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesRecyclerAdapter
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
        val layoutManager = binding.recycleViewMovies.layoutManager as LinearLayoutManager

        // Add a scroll listener to get more items if the user reaches the bottom of the list
        binding.recycleViewMovies.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0 && viewModel.loading.value == false){
                    if(layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1){
                        if(viewModel.canFetchMovies()){
                            Snackbar.make(
                                binding.recycleViewMovies,
                                "You have seen all movies for today!",
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }else{
                            viewModel.getNowPlayingMovies()
                        }
                    }
                }
            }
        })

        viewModel.getNowPlayingMovies()
        viewModel.movies.observe(viewLifecycleOwner, Observer {

            it.let {
                if(it.any()){
                    moviesAdapter.submitList(it)
                    Timber.i("Movie count: ${it.size}")
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetList()
    }
}