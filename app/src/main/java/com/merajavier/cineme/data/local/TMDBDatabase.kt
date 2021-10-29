package com.merajavier.cineme.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovieEntity::class], version = 2, exportSchema = false)
abstract class TMDBDatabase : RoomDatabase(){

    abstract val favoriteMovieDao: FavoriteMovieDao

    companion object {
        @Volatile
        private var INSTANCE: TMDBDatabase? = null

        fun getInstance(context: Context) : TMDBDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TMDBDatabase::class.java,
                        "tmdb_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }
        }
    }
}