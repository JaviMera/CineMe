package com.merajavier.cineme

import android.app.Application
import android.content.SharedPreferences
import androidx.paging.ExperimentalPagingApi
import androidx.work.*
import com.merajavier.cineme.cast.CastListViewModel
import com.merajavier.cineme.data.local.*
import com.merajavier.cineme.koin.modules.networkModule
import com.merajavier.cineme.koin.modules.sharedPreferencesModule
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.login.account.AccountViewModel
import com.merajavier.cineme.movies.MovieListViewModel
import com.merajavier.cineme.movies.search.SearchMoviesFragmentDirections
import com.merajavier.cineme.movies.search.SearchMoviesViewModel
import com.merajavier.cineme.movies.upcoming.UpcomingMoviesViewModel
import com.merajavier.cineme.network.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber

@ExperimentalPagingApi
class Application : Application(), KoinComponent, Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        val viewModelModule = module {
            viewModel{
                MovieListViewModel(get() as NetworkMoviesRepositoryInterface,
                get() as TMDBDatabase)
            }

            viewModel{
                CastListViewModel(get() as NetworkMovieActorRepository)
            }

            viewModel () {
                LoginViewModel(
                    get() as NetworkAccountRepositoryInterface,
                    get() as NetworkAuthenticationRepositoryInterface,
                    get() as LocalAccountRepositoryInterface,
                    get() as SharedPreferences)
            }

            viewModel {
                AccountViewModel(
                    get() as NetworkAccountRepositoryInterface,
                    get() as LocalAccountRepositoryInterface
                )
            }

            viewModel{
                UpcomingMoviesViewModel(get())
            }

            viewModel {
                SearchMoviesViewModel(
                    get() as NetworkSearchRepositoryInterface,
                    get() as NetworkMoviesRepositoryInterface)
            }

            single {
                NetworkAuthenticationRepository(get() as TMDBApiAuthenticationInterface) as NetworkAuthenticationRepositoryInterface
            }

            single{
                NetworkMoviesRepository(get() as TMDBApMoviesiInterface) as NetworkMoviesRepositoryInterface
            }

            single{
                NetworkMovieActorRepository(get() as TMDBApiCastInterface)
            }

            single{
                NetworkAccountRepository(get() as TMDBApiAccountInterface) as NetworkAccountRepositoryInterface
            }

            single {
                NetworkSearchRepository(get() as TMDBApiSearchInterface) as NetworkSearchRepositoryInterface
            }

            single{
                LocalAccountRepository(
                    get() as FavoriteMovieDao,
                    get() as UserSessionDao) as LocalAccountRepositoryInterface
            }

            single {
                TMDBDatabase.getInstance(this@Application).favoriteMovieDao
            }

            single {
                TMDBDatabase.getInstance(this@Application).userSessionDao
            }

            single {
                TMDBDatabase.getInstance(this@Application).nowPlayingMoviesDao
            }

            single {
                TMDBDatabase.getInstance(this@Application).remoteKeysDao
            }

            single {
                TMDBDatabase.getInstance(this@Application) as TMDBDatabase
            }

            worker {UpcomingMoviesWorker(get(), get(), get() as NetworkMoviesRepositoryInterface)}
            worker { NowPlayingMoviesWorker(get(), get(), get() as NetworkMoviesRepositoryInterface, get() as TMDBDatabase) }
        }

        startKoin {
            androidContext(this@Application)
            modules(viewModelModule, networkModule, sharedPreferencesModule)
            workManagerFactory()
        }

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {

        val config = Configuration.Builder()
            .build()

        WorkManager.initialize(this, config)
        return config
    }
}