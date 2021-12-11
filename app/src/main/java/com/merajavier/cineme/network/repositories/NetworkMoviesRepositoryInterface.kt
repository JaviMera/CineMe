package com.merajavier.cineme.network.repositories

import com.google.gson.Gson
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.AccountStateResponse
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesResponse
import com.merajavier.cineme.movies.favorites.FavoriteMoviesResponse
import com.merajavier.cineme.movies.rate.RateMovieRequest
import com.merajavier.cineme.movies.rate.RateMovieResponse
import com.merajavier.cineme.movies.reviews.MovieReviewsResponse
import com.merajavier.cineme.network.api.TMDBApMoviesiInterface
import retrofit2.*
import timber.log.Timber

interface NetworkMoviesRepositoryInterface {

    suspend fun getNowPlaying(pageNumber: Int) : TMDBApiResult<*>
    suspend fun getDetails(movieId: Int) : TMDBApiResult<*>
    suspend fun  getUpcoming(pageNumber: Int) : TMDBApiResult<*>
    suspend fun getAccountState(movieId: Int, sessionId: String) : TMDBApiResult<*>
    suspend fun getReviews(movieId: Int, pageNumber: Int) : TMDBApiResult<*>
    suspend fun rate(movieId: Int, rateMovieRequest: RateMovieRequest, sessionId: String) : TMDBApiResult<*>
}

class NetworkMoviesRepository(
    private val apiMoviesiInterface: TMDBApMoviesiInterface
    ) : NetworkMoviesRepositoryInterface {

    override suspend fun getNowPlaying(pageNumber: Int): TMDBApiResult<*> {
        return try {
            val response = apiMoviesiInterface.getNowPlayingMovies(pageNumber).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), MoviesResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun getDetails(movieId: Int): TMDBApiResult<*> {
        return try {
            val response = apiMoviesiInterface.getMovie(movieId).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), MovieDataItem::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun getUpcoming(pageNumber: Int): TMDBApiResult<*> {
        return try {
            val response = apiMoviesiInterface.getUpcomingMovies(pageNumber).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), MoviesResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun getAccountState(movieId: Int, sessionId: String): TMDBApiResult<*> {
        return try {
            val response = apiMoviesiInterface.getAccountState(movieId, sessionId).awaitResponse()
            if(response.isSuccessful){
                TMDBApiResult.Success(Gson().fromJson(response.body(), AccountStateResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch (exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun getReviews(movieId: Int, pageNumber: Int): TMDBApiResult<*> {
        return try{
            val response = apiMoviesiInterface.getReviews(movieId, pageNumber).awaitResponse()
            if(response.isSuccessful){
                Timber.i(response.body())
                TMDBApiResult.Success(Gson().fromJson(response.body(), MovieReviewsResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch(exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }

    override suspend fun rate(movieId: Int, rateMovieRequest: RateMovieRequest, sessionId: String): TMDBApiResult<*> {
        return try{
            val response = apiMoviesiInterface.rate(movieId, rateMovieRequest, sessionId).awaitResponse()
            if(response.isSuccessful){
                Timber.i(response.body())
                TMDBApiResult.Success(Gson().fromJson(response.body(), RateMovieResponse::class.java))
            }else{
                TMDBApiResult.Failure(Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java))
            }
        }catch(exception: Exception){
            TMDBApiResult.Error(exception.localizedMessage)
        }
    }
}

