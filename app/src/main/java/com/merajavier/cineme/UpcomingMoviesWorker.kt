package com.merajavier.cineme

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MoviesResponse
import com.merajavier.cineme.network.repositories.NetworkMoviesRepositoryInterface
import org.koin.core.component.KoinComponent
import timber.log.Timber
import java.lang.Exception

class UpcomingMoviesWorker(
  context: Context,
  params: WorkerParameters,
  private val networkMoviesRepository: NetworkMoviesRepositoryInterface
) : CoroutineWorker(context, params), KoinComponent{

    override suspend fun doWork(): Result {

        return try{
            when(val response = networkMoviesRepository.getUpcoming(1)) {
                is TMDBApiResult.Success -> {
                    val upcomingMovies = response.data as MoviesResponse
                    if(upcomingMovies.movies.any()){
                        sendNotification(applicationContext, upcomingMovies.movies.take(3).map { movie -> movie.title })
                    }
                }
                is TMDBApiResult.Error -> {
                    Timber.i("Unable to get upcoming movies: ${response.message}")
                }
                is TMDBApiResult.Failure -> {
                    val upcomingMoviesfailure = response.data as ErrorResponse
                    Timber.i(upcomingMoviesfailure.statusMessage)
                }
            }

            Result.success()
        }catch(exception: Exception){

            Result.failure()
        }
    }

    companion object{
        const val WORKER_NAME = "UpcomingMoviesWorker"
    }
}