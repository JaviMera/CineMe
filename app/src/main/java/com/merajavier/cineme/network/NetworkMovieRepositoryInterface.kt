package com.merajavier.cineme.network

import com.merajavier.cineme.genre.RateMovieRequest
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.rate.RateMovieResponse

interface NetworkMovieRepositoryInterface<T>{

    suspend fun getAll(pageNumber: Int) : List<T>
    suspend fun getDetails(movieId: Int) : T
    suspend fun rateMovieAsGuest(movieId: Int, sessionId: String, rateMovieRequest: RateMovieRequest) : RateMovieResponse
}

class NetworkMovieRepository(
    private val apiInterface: TMDBApiInterface
) : NetworkMovieRepositoryInterface<MovieDataItem>{

    override suspend fun getAll(pageNumber: Int): List<MovieDataItem> {
        val response = apiInterface.getNowPlayingMovies(pageNumber)
        return response.movies
    }

    override suspend fun getDetails(movieId: Int): MovieDataItem {
        return apiInterface.getMovie(movieId)
    }

    override suspend fun rateMovieAsGuest(movieId: Int, sessionId: String, rateMovieRequest: RateMovieRequest): RateMovieResponse {
        return apiInterface.rateMovieAsGuest(movieId, sessionId, rateMovieRequest)
    }
}