package com.merajavier.cineme.movies

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.merajavier.cineme.movies.search.SearchMoviesPagingSource
import com.merajavier.cineme.movies.upcoming.UpcomingMoviesPagingSource
import com.merajavier.cineme.network.NetworkMoviesRepositoryInterface
import com.merajavier.cineme.network.NetworkSearchRepositoryInterface
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class MoviesPagerRepository(){
    fun nowPlayingMoviesPagingData(
        networkMoviesRepositoryInterface: NetworkMoviesRepositoryInterface
    ) : LiveData<PagingData<MovieDataItem>> {

        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { MoviesPagingSource(networkMoviesRepositoryInterface) }
        ).liveData
    }

    fun upcomingPlayingMoviesPagingData(
        networkMoviesRepositoryInterface: NetworkMoviesRepositoryInterface
    ) : LiveData<PagingData<MovieDataItem>>{
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { UpcomingMoviesPagingSource(networkMoviesRepositoryInterface) }
        ).liveData
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 1, enablePlaceholders = false)
    }

    fun searchMoviesPagingData(
        networkMoviesRepositoryInterface: NetworkSearchRepositoryInterface,
        movieTitle: String): LiveData<PagingData<MovieDataItem>> {
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { SearchMoviesPagingSource(networkMoviesRepositoryInterface, movieTitle) }
        ).liveData
    }
}