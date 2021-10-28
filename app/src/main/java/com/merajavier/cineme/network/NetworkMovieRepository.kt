package com.merajavier.cineme.network

import com.merajavier.cineme.movies.MovieDataItem

interface NetworkRepositoryInterface<T>{

    suspend fun getAll(pageNumber: Int) : List<T>
    suspend fun getDetails(movieId: Int) : T
}
class NetworkMovieRepository(
    private val apiInterface: TMDBApiInterface
    ) : NetworkRepositoryInterface<MovieDataItem>{

    override suspend fun getAll(pageNumber: Int): List<MovieDataItem> {
        val response = apiInterface.getNowPlayingMovies(pageNumber)

        return response.movies
    }

    override suspend fun getDetails(movieId: Int): MovieDataItem {
        return apiInterface.getMovie(movieId)
    }
}

