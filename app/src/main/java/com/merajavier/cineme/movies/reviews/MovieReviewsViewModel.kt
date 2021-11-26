package com.merajavier.cineme.movies.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MoviesPagerRepository
import com.merajavier.cineme.movies.SingleLiveData
import com.merajavier.cineme.network.repositories.NetworkMoviesRepositoryInterface
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieReviewsViewModel(
    private val networkMoviesRepositoryInterface: NetworkMoviesRepositoryInterface
) : ViewModel () {

    private val _topReviews = 5
    private val _topMovieReviews = SingleLiveData<List<ReviewDataItem>>()
    val topMovieReviews: LiveData<List<ReviewDataItem>>
    get() = _topMovieReviews

    @ExperimentalPagingApi
    fun fetchReviews(movieId : Int) : LiveData<PagingData<ReviewDataItem>>{
        return MoviesPagerRepository()
            .reviewsPagingData(networkMoviesRepositoryInterface, movieId)
            .map {
                it.map { review -> review }
            }
            .cachedIn(viewModelScope)
    }

    fun getTopMovieReviews(movieId: Int) {
        viewModelScope.launch {
            try {
                when (val result = networkMoviesRepositoryInterface.getReviews(movieId, 1)) {
                    is TMDBApiResult.Success -> {
                        val response = result.data as MovieReviewsResponse
                        response.results?.let { reviews ->
                            reviews
                                .sortedByDescending { review -> review.authorDetails?.rating }
                                .let { topReviews ->
                                    _topMovieReviews.postValue(topReviews.take(_topReviews))
                                }
                        }
                    }
                    is TMDBApiResult.Failure -> {
                        val failureResponse = result.data as ErrorResponse
                        Timber.i(failureResponse.statusMessage)
                    }
                    is TMDBApiResult.Error -> {
                    }
                    else -> {
                        Timber.i("Initializing ")
                    }
                }
            } catch (exception: Exception) {
                Timber.i(exception.localizedMessage)
            }
        }
    }
}