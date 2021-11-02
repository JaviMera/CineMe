package com.merajavier.cineme.movies.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.paging.ExperimentalPagingApi
import com.merajavier.cineme.databinding.FragmentSearchMoviesBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchMoviesFragment : Fragment() {

    private lateinit var binding: FragmentSearchMoviesBinding

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

        binding.fragmentSearchEditText.doOnTextChanged { text, start, before, count ->
            Toast.makeText(requireContext(), text.toString(), Toast.LENGTH_SHORT)
                .show()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}