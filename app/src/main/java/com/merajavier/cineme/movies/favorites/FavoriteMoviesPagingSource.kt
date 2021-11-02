package com.merajavier.cineme.movies.favorites

import androidx.paging.PagingSource
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.network.NetworkAccountRepositoryInterface

class FavoriteMoviesPagingSource (
    private val networkAccountRepositoryInterface: NetworkAccountRepositoryInterface,
    private val sessionId: String,
    private val accountId: Int
) : PagingSource<Int, FavoriteMovieDataItem>() {

    private var maxPages: Int = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavoriteMovieDataItem> {
        return try {
            val page = params.key ?: 1
            if(page <= maxPages) {
                when (val result = networkAccountRepositoryInterface.getFavoriteMovies(accountId, sessionId, page)) {
                    is TMDBApiResult.Success -> {
                        val response = result.data as FavoriteMoviesResponse
                        maxPages = response.totalPages
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
            }else{
                throw Exception("You've reached the end!")
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}