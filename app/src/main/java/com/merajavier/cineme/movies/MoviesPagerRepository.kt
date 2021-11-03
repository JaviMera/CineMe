package com.merajavier.cineme.movies

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.merajavier.cineme.data.local.MovieEntity
import com.merajavier.cineme.data.local.NowPlayingMoviesDao
import com.merajavier.cineme.data.local.RemoteKeysDao
import com.merajavier.cineme.movies.favorites.FavoriteMovieDataItem
import com.merajavier.cineme.movies.favorites.FavoriteMoviesPagingSource
import com.merajavier.cineme.movies.search.SearchMoviesPagingSource
import com.merajavier.cineme.movies.upcoming.UpcomingMoviesPagingSource
import com.merajavier.cineme.network.NetworkAccountRepositoryInterface
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

    fun nowPlayingMoviesDb(
        networkMoviesRepositoryInterface: NetworkMoviesRepositoryInterface,
        moviesDao: NowPlayingMoviesDao,
        remoteKeysDao: RemoteKeysDao
    ) : LiveData<PagingData<MovieEntity>> {
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { moviesDao.getMovies() },
            remoteMediator = NowPlayingMoviesMediator(networkMoviesRepositoryInterface, moviesDao, remoteKeysDao)
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