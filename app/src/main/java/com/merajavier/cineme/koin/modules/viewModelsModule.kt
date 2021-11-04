package com.merajavier.cineme.koin.modules

import android.content.SharedPreferences
import androidx.paging.ExperimentalPagingApi
import com.merajavier.cineme.cast.CastListViewModel
import com.merajavier.cineme.data.local.LocalAccountRepositoryInterface
import com.merajavier.cineme.data.local.TMDBDatabase
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.login.account.AccountViewModel
import com.merajavier.cineme.movies.MovieListViewModel
import com.merajavier.cineme.movies.search.SearchMoviesViewModel
import com.merajavier.cineme.movies.upcoming.UpcomingMoviesViewModel
import com.merajavier.cineme.network.*
import com.merajavier.cineme.network.repositories.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalPagingApi
val viewModelsModule = module{
    viewModel{
        MovieListViewModel(get() as NetworkMoviesRepositoryInterface,
            get() as TMDBDatabase
        )
    }

    viewModel{
        CastListViewModel(get() as NetworkMovieActorRepository)
    }

    viewModel () {
        LoginViewModel(
            get() as NetworkAccountRepositoryInterface,
            get() as NetworkAuthenticationRepositoryInterface,
            get() as LocalAccountRepositoryInterface,
            get() as SharedPreferences
        )
    }

    viewModel {
        AccountViewModel(
            get() as NetworkAccountRepositoryInterface
        )
    }

    viewModel{
        UpcomingMoviesViewModel(get())
    }

    viewModel {
        SearchMoviesViewModel(
            get() as NetworkSearchRepositoryInterface,
            get() as NetworkMoviesRepositoryInterface
        )
    }
}