package com.merajavier.cineme.movies.reviews

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.network.repositories.NetworkMoviesRepositoryInterface
import timber.log.Timber

class MovieReviewsPagingSource(
    private val apiInterface: NetworkMoviesRepositoryInterface,
    private val movieId: Int
) : PagingSource<Int, ReviewDataItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewDataItem> {
        return try {
            val page = params.key ?: 1
            when (val result = apiInterface.getReviews(movieId, page)) {
                is TMDBApiResult.Success -> {
                    val response = result.data as MovieReviewsResponse

                    LoadResult.Page(
                        response.results!!,
                        if (page == 1) null else page - 1,
                        if (response.results.isEmpty()) null else page.inc()
                    )
                }
                is TMDBApiResult.Failure -> {
                    val failureResponse = result.data as ErrorResponse
                    LoadResult.Error(Exception(failureResponse.statusMessage))
                }
                is TMDBApiResult.Error -> {
                    LoadResult.Error(Exception(result.message))
                }
                TMDBApiResult.Init -> LoadResult.Page(
                    listOf(), null, null
                )
            }
        } catch (exception: Exception) {
            Timber.i(exception.localizedMessage)
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ReviewDataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}