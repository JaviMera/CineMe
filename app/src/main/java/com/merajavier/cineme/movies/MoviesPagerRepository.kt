package com.merajavier.cineme.movies

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.merajavier.cineme.data.local.TMDBDatabase
import com.merajavier.cineme.movies.favorites.FavoriteMovieDataItem
import com.merajavier.cineme.movies.favorites.FavoriteMoviesPagingSource
import com.merajavier.cineme.movies.reviews.MovieReviewsPagingSource
import com.merajavier.cineme.movies.reviews.ReviewDataItem
import com.merajavier.cineme.movies.search.SearchMoviesPagingSource
import com.merajavier.cineme.movies.upcoming.UpcomingMoviesPagingSource
import com.merajavier.cineme.network.repositories.NetworkAccountRepositoryInterface
import com.merajavier.cineme.network.repositories.NetworkMoviesRepositoryInterface
import com.merajavier.cineme.network.repositories.NetworkSearchRepositoryInterface

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

    fun localMoviesPagingData(
        tmdbDatabase: TMDBDatabase
    ) : LiveData<PagingData<MovieDataItem>> {

        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { LocalMoviesPagingSource(tmdbDatabase) }
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

    fun favoriteMoviesPagingData(
        networkAccountRepositoryInterface: NetworkAccountRepositoryInterface,
        sessionId: String,
        accountId: Int
    ) : LiveData<PagingData<FavoriteMovieDataItem>>{
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { FavoriteMoviesPagingSource(networkAccountRepositoryInterface, sessionId, accountId) }
        ).liveData
    }

    fun searchMoviesPagingData(
        networkMoviesRepositoryInterface: NetworkSearchRepositoryInterface,
        movieTitle: String): LiveData<PagingData<MovieDataItem>> {
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { SearchMoviesPagingSource(networkMoviesRepositoryInterface, movieTitle) }
        ).liveData
    }

    fun reviewsPagingData(
        networkMoviesRepositoryInterface: NetworkMoviesRepositoryInterface,
        movieId: Int
    ) : LiveData<PagingData<ReviewDataItem>>{

        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { MovieReviewsPagingSource(networkMoviesRepositoryInterface, movieId)}
        ).liveData
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 1, enablePlaceholders = false)
    }
}