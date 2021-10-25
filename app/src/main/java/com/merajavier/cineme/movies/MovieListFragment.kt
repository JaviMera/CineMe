package com.merajavier.cineme.movies

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentMoviesBinding
import com.merajavier.cineme.details.DetailsActivity
import com.merajavier.cineme.login.LoginViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesRecyclerAdapter
    private val _viewModel: MovieListViewModel by inject()
    private val _loginViewModel: LoginViewModel by inject()

    private var _pageNumber = 1

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
        binding.viewModel = _viewModel

        moviesAdapter = MoviesRecyclerAdapter(MoviesRecyclerAdapter.OnMovieClickListener{
            _viewModel.getMovieDetails(it)
        })

        binding.recycleViewMovies.adapter = moviesAdapter
        val layoutManager = binding.recycleViewMovies.layoutManager as LinearLayoutManager

        // Add a scroll listener to get more items if the user reaches the bottom of the list
        binding.recycleViewMovies.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0 && _viewModel.loading.value == false){
                    if(layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1){
                        _pageNumber = _pageNumber.inc()
                        _viewModel.getNowPlayingMovies(_pageNumber)
                    }
                }
            }
        })

        _viewModel.getNowPlayingMovies(_pageNumber)

        _viewModel.movies.observe(viewLifecycleOwner, Observer {

            it.let {
                if(it.any()){
                    moviesAdapter.submitList(it)
                }else{
                    Toast.makeText(requireContext(), getString(R.string.get_movies_error), Toast.LENGTH_SHORT).show()
                }
            }
        })

        _viewModel.loading.observe(viewLifecycleOwner, Observer {
            it.let {
                when(it){
                    true -> {
                        Timber.i("loading")
                        binding.loadingIndicator.visibility = View.VISIBLE
                    }
                    false -> {
                        Timber.i("not loading")
                        binding.loadingIndicator.visibility = View.GONE
                    }
                }
            }
        })

        _viewModel.movieSelected.observe(viewLifecycleOwner, Observer {

            it.let {
                val intent = Intent(requireActivity(), DetailsActivity::class.java).apply {
                    putExtra(DetailsActivity.SELECTED_MOVIE_ID, it)
                }

                startActivity(intent)
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }
}