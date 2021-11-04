package com.merajavier.cineme.movies

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.merajavier.cineme.data.local.TMDBDatabase
import com.merajavier.cineme.data.local.toMovieDataItems

class LocalMoviesPagingSource(
    private val tmdbDatabase: TMDBDatabase
) : PagingSource<Int, MovieDataItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDataItem> {
        return try{
            tmdbDatabase.withTransaction {
                LoadResult.Page(
                    tmdbDatabase.nowPlayingMoviesDao.getMovieEntities().toMovieDataItems(),
                    params.key, params.key?.inc()
                )
            }
        }catch(exception: Exception){
            LoadResult.Error(exception)
        }
    }
}