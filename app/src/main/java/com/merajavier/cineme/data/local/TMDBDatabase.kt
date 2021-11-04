package com.merajavier.cineme.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.merajavier.cineme.data.local.daos.NowPlayingMoviesDao
import com.merajavier.cineme.data.local.daos.RemoteKeysDao
import com.merajavier.cineme.data.local.daos.UserSessionDao
import com.merajavier.cineme.data.local.entities.MovieEntity
import com.merajavier.cineme.data.local.entities.RemoteKeysEntity
import com.merajavier.cineme.data.local.entities.UserSessionEntity

@Database(
    entities = [UserSessionEntity::class, MovieEntity::class, RemoteKeysEntity::class],
    version = 8,
    exportSchema = false)
abstract class TMDBDatabase : RoomDatabase(){

    abstract val userSessionDao: UserSessionDao
    abstract val nowPlayingMoviesDao: NowPlayingMoviesDao
    abstract val remoteKeysDao: RemoteKeysDao

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