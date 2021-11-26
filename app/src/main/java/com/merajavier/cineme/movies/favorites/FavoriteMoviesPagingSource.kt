package com.merajavier.cineme.movies.favorites

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.network.repositories.NetworkAccountRepositoryInterface

class FavoriteMoviesPagingSource (
    private val networkAccountRepositoryInterface: NetworkAccountRepositoryInterface,
    private val sessionId: String,
    private val accountId: Int
) : PagingSource<Int, FavoriteMovieDataItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavoriteMovieDataItem> {
        return try {
            val page = params.key ?: 1
            when (val result =
                networkAccountRepositoryInterface.getFavoriteMovies(accountId, sessionId, page)) {
                is TMDBApiResult.Success -> {
                    val response = result.data as FavoriteMoviesResponse
                    LoadResult.Page(
                        response.movies,
                        if (page == 1) null else page - 1,
                        if (response.movies.isEmpty()) null else page.inc()
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
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FavoriteMovieDataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}