package com.merajavier.cineme.movies

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.merajavier.cineme.databinding.FragmentMoviesBinding
import org.koin.android.ext.android.inject

class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesRecyclerAdapter
    private val _viewModel: MoviesViewModel by inject()

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

        moviesAdapter = MoviesRecyclerAdapter()
        binding.recycleViewMovies.adapter = moviesAdapter

        _viewModel.movies.observe(viewLifecycleOwner, Observer {

            it.let {
                if(it.any()){
                    moviesAdapter.submitList(it)
                }else{
                    Toast.makeText(requireContext(), "Unable to load list of movies", Toast.LENGTH_SHORT).show()
                }
            }
        })

        _viewModel.loading.observe(viewLifecycleOwner, Observer {
            it.let {
                when(it){
                    true -> {
                        Log.i("MoviesListFragment", "loading")
                        binding.loadingIndicator.visibility = View.VISIBLE
                    }
                    false -> {
                        Log.i("MoviesListFragment", "not loading")
                        binding.loadingIndicator.visibility = View.GONE
                    }
                }
            }
        })
        return binding.root
    }
}