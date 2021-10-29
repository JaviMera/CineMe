package com.merajavier.cineme.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteMovieEntity::class, UserSessionEntity::class],
    version = 4,
    exportSchema = false)
abstract class TMDBDatabase : RoomDatabase(){

    abstract val favoriteMovieDao: FavoriteMovieDao
    abstract val userSessionDao: UserSessionDao

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