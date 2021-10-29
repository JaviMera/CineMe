package com.merajavier.cineme.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import retrofit2.http.DELETE

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorites")
    fun getFavorites() : LiveData<List<FavoriteMovieEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addMovies(movieEntity: List<FavoriteMovieEntity>)

    @Delete(entity = FavoriteMovieEntity::class)
    suspend fun deleteMovie(movie: FavoriteMovieEntity)
}

