package com.merajavier.cineme.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.merajavier.cineme.movies.favorites.FavoriteMovieDataItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface LocalAccountRepositoryInterface{

    val favoriteMovies: LiveData<List<FavoriteMovieDataItem>>

    suspend fun getFavorites() : LiveData<List<FavoriteMovieEntity>>
    suspend fun addFavoriteMovies(favorites: List<FavoriteMovieEntity>)
}

class LocalAccountRepository(
    private val favoritesDao: FavoriteMovieDao,
    private val iosDispatcher: CoroutineDispatcher = Dispatchers.IO
)
    : LocalAccountRepositoryInterface{

    override val favoriteMovies: LiveData<List<FavoriteMovieDataItem>> = Transformations.map(favoritesDao.getFavorites()){
            it.map { movieEntity ->
                FavoriteMovieDataItem(
                    id = movieEntity.movieId,
                    title = movieEntity.title!!,
                    overview = movieEntity.overview!!,
                    releaseDate = movieEntity.releaseDate!!,
                    posterPath = movieEntity.posterPath!!
                )
            }
        }

    override suspend fun getFavorites(): LiveData<List<FavoriteMovieEntity>> = withContext(iosDispatcher){
        return@withContext favoritesDao.getFavorites()
    }

    override suspend fun addFavoriteMovies(favorites: List<FavoriteMovieEntity>) = withContext(iosDispatcher){
        favoritesDao.insertFavorites(favorites)
    }
}