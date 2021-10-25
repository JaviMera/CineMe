package com.merajavier.cineme.network

import com.merajavier.cineme.cast.ActorDataItem
import com.merajavier.cineme.cast.CrewDataItem

interface NetworkCastRepositoryInterface<TActor, TCrew> {
    suspend fun getActors(movieId: Int) : List<TActor>
    suspend fun getCrew(movieId: Int) : List<TCrew>
}

class NetworkMovieActorRepository(
    private val apiInterface: TMDBApiCastInterface
) : NetworkCastRepositoryInterface<ActorDataItem, CrewDataItem>{

    override suspend fun getActors(movieId: Int): List<ActorDataItem> {
        return apiInterface.getMovieCast(movieId)
            .cast
            .sortedBy { actor -> actor.order }
    }

    override suspend fun getCrew(movieId: Int): List<CrewDataItem> {
        return apiInterface.getMovieCast(movieId)
            .crew
    }
}