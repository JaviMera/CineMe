package com.merajavier.cineme.movies.reviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.merajavier.cineme.R
import com.merajavier.cineme.databinding.FragmentReviewsBinding
import com.merajavier.cineme.movies.MoviesFooterAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalPagingApi
class ReviewsFragment : Fragment() {

    private lateinit var binding: FragmentReviewsBinding
    private val reviewsViewModel: MovieReviewsViewModel by viewModel()
    private val args: ReviewsFragmentArgs by navArgs()

    private lateinit var reviewsAdapter: ReviewsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false)
        binding.lifecycleOwner = this
        reviewsAdapter = ReviewsPagerAdapter()

        binding.reviewsRecyclerView.adapter = reviewsAdapter.withLoadStateFooter(MoviesFooterAdapter())
        reviewsViewModel.fetchReviews(args.movieId).observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                reviewsAdapter.submitData(it)
            }
        })

        return binding.root
    }
}