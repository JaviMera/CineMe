package com.merajavier.cineme.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.merajavier.cineme.data.local.entities.MovieEntity

@Dao
interface NowPlayingMoviesDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movieEntity: List<MovieEntity>)

    @Query("SELECT * FROM now_playing_movies")
    fun getMovieEntities() : List<MovieEntity>

    @Query("DELETE FROM now_playing_movies")
    suspend fun clearAll()
}