package com.merajavier.cineme.network

import com.google.gson.Gson
import com.merajavier.cineme.BuildConfig
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesResponse
import com.merajavier.cineme.movies.upcoming.UpcomingMovieResponse
import retrofit2.*
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber

interface NetworkRepositoryInterface<T>{

    suspend fun getAll(pageNumber: Int) : List<T>
    suspend fun getDetails(movieId: Int) : T
    suspend fun  getUpcoming(pageNumber: Int) : TMDBApiResult<*>
}

class NetworkMovieRepository(
    private val apiMoviesiInterface: TMDBApMoviesiInterface
    ) : NetworkRepositoryInterface<MovieDataItem>{

    override suspend fun getAll(pageNumber: Int): List<MovieDataItem> {
        val response = apiMoviesiInterface.getNowPlayingMovies(pageNumber)

        return response.movies
    }

    override suspend fun getDetails(movieId: Int): MovieDataItem {
        return apiMoviesiInterface.getMovie(movieId)
    }

    override suspend fun getUpcoming(pageNumber: Int): TMDBApiResult<*> {
        return try {
            val response = apiMoviesiInterface.getUpcomingMovies(pageNumber).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), UpcomingMovieResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }
}

