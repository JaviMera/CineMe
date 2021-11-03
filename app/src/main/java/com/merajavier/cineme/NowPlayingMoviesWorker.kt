package com.merajavier.cineme

import android.content.Context
import androidx.room.withTransaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.data.local.TMDBDatabase
import com.merajavier.cineme.data.local.toMovieEntities
import com.merajavier.cineme.movies.MoviesResponse
import com.merajavier.cineme.network.NetworkMoviesRepositoryInterface
import org.koin.core.component.KoinComponent
import timber.log.Timber
import java.lang.Exception

class NowPlayingMoviesWorker(
    context: Context,
    params: WorkerParameters,
    private val networkMoviesRepository: NetworkMoviesRepositoryInterface,
    private val tmdbDatabase: TMDBDatabase
) : CoroutineWorker(context, params), KoinComponent {

    override suspend fun doWork(): Result {
        return try{

            // Save the first page of movies from the NowPlaying endpoint
            when(val result = networkMoviesRepository.getNowPlaying(1)) {
                is TMDBApiResult.Success -> {
                    val response = result.data as MoviesResponse
                    tmdbDatabase.withTransaction {
                        Timber.i("Saving ${response.movies.size}")
                        val moviesDao = tmdbDatabase.nowPlayingMoviesDao
                        moviesDao.addMovies(response.movies.toMovieEntities())
                        moviesDao.getMovieEntities().forEach { movie -> Timber.i(movie.title) }
                    }
                }
                is TMDBApiResult.Error -> {
                    Timber.i(result.message)
                    Result.failure()
                }
                is TMDBApiResult.Failure -> {
                    val upcomingMoviesFailure = result.data as ErrorResponse
                    Timber.i(upcomingMoviesFailure.statusMessage)
                    Result.failure()
                }
            }

            Result.success()
        }catch(exception: Exception){
            Result.failure()
        }
    }

    companion object{
        const val WORKER_NAME = "NowPlayingMoviesWorker"
    }
}