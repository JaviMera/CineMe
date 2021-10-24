package com.merajavier.cineme.network

import com.merajavier.cineme.cast.ActorDataItem

interface NetworkCastRepositoryInterface<T> {
    suspend fun getAll(movieId: Int) : List<T>
}

class NetworkMovieActorRepository(
    private val apiInterface: TMDBApiCastInterface
) : NetworkCastRepositoryInterface<ActorDataItem>{

    override suspend fun getAll(movieId: Int): List<ActorDataItem> {
        return apiInterface.getMovieCast(movieId)
            .cast
            .sortedBy { actor -> actor.order }
    }
}