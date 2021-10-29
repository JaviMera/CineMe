package com.merajavier.cineme.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorites")
    fun getFavorites() : LiveData<List<FavoriteMovieEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertFavorites(movieEntity: List<FavoriteMovieEntity>)
}

