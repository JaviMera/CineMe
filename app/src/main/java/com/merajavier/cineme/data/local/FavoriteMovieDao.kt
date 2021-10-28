package com.merajavier.cineme.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorites")
    suspend fun getFavorites(username: String) : List<FavoriteMovieDto>
}

