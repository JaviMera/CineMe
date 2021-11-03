package com.merajavier.cineme.movies

import androidx.paging.PagingSource
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.data.local.NowPlayingMoviesDao
import com.merajavier.cineme.data.local.TMDBDatabase
import com.merajavier.cineme.data.local.toMovieEntities
import com.merajavier.cineme.network.NetworkMoviesRepositoryInterface
import timber.log.Timber
import kotlin.math.max

class MoviesPagingSource(
    private val apiInterface: NetworkMoviesRepositoryInterface
) : PagingSource<Int, MovieDataItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDataItem> {
        return try {
            val page = params.key ?: 1
            when (val result = apiInterface.getNowPlaying(page)) {
                is TMDBApiResult.Success -> {
                    val response = result.data as MoviesResponse

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
            Timber.i(exception.localizedMessage)
            LoadResult.Error(exception)
        }
    }
}