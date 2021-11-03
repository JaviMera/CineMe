package com.merajavier.cineme.movies

import androidx.paging.*
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.data.local.*
import com.merajavier.cineme.network.NetworkMoviesRepositoryInterface
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class NowPlayingMoviesMediator(
    private val networkMoviesRepositoryInterface: NetworkMoviesRepositoryInterface,
    private val moviesDao: NowPlayingMoviesDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val initialPage: Int = 1
) : RemoteMediator<Int, MovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {

        return try {

            val page = when(loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getClosestRemoteKeys(state) ?: throw InvalidObjectException("Unable to refresh")
                    remoteKey.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKey = getLastRemoteKey(state)
                    remoteKey?.nextKey?.minus(1) ?: initialPage
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            when (val result = networkMoviesRepositoryInterface.getNowPlaying(page)) {
                is TMDBApiResult.Success -> {
                    val response = result.data as MoviesResponse
                    val endOfPagination = page >= response.totalPages

                    if(loadType == LoadType.REFRESH){
                        remoteKeysDao.clearRemoteKeys()
                        moviesDao.clearAll()
                    }

                    val prevKey = if (page == initialPage) null else page - 1
                    val nextKey = if (endOfPagination) null else page + 1
                    Timber.i("Previous key: $prevKey")
                    Timber.i("Next key: $nextKey")

                    val keys = response.movies.map { movie ->
                        RemoteKeysEntity(repoId = movie.id, prevKey = prevKey, nextKey = nextKey)
                    }

                    remoteKeysDao.insertAll(keys)
                    moviesDao.addMovies(response.movies.toMovieEntities())

                    MediatorResult.Success(endOfPaginationReached = endOfPagination)
                }
                is TMDBApiResult.Failure -> {
                    val failureResponse = result.data as ErrorResponse
                    MediatorResult.Error(Exception(failureResponse.statusMessage))
                }
                is TMDBApiResult.Error -> {
                    MediatorResult.Error(Exception(result.message))
                }
                TMDBApiResult.Init -> MediatorResult.Error(Exception("Something went wrong"))
            }
        }catch(exception: IOException){
            MediatorResult.Error(exception)
        }catch(exception: HttpException){
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getClosestRemoteKeys(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {

        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let { movieEntity ->
                remoteKeysDao.getMovieRemoteKeys(movieEntity.movieId)
            }
        }

    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.lastItemOrNull()?.let { movieEntity ->
            val key = remoteKeysDao.getMovieRemoteKeys(movieEntity.movieId)
            Timber.i("Last remote key: $key")
            key
        }
    }
}