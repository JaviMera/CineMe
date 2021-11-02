package com.merajavier.cineme.movies.search

import androidx.paging.PagingSource
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesResponse
import com.merajavier.cineme.network.NetworkMoviesRepositoryInterface
import com.merajavier.cineme.network.NetworkSearchRepositoryInterface

class SearchMoviesPagingSource(
    private val apiInterface: NetworkSearchRepositoryInterface,
    private val title: String
) : PagingSource<Int, MovieDataItem>() {

    private var maxPages: Int = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDataItem> {
        return try {
            val page = params.key ?: 1
            if(page <= maxPages) {
                when (val result = apiInterface.searchMoviesByTitle(title)) {
                    is TMDBApiResult.Success -> {
                        val response = result.data as MoviesResponse
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