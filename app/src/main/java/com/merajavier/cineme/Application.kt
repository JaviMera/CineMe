package com.merajavier.cineme

import android.app.Application
import android.content.SharedPreferences
import androidx.work.*
import com.merajavier.cineme.cast.CastListViewModel
import com.merajavier.cineme.data.local.*
import com.merajavier.cineme.koin.modules.networkModule
import com.merajavier.cineme.koin.modules.sharedPreferencesModule
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.login.account.AccountViewModel
import com.merajavier.cineme.movies.MovieListViewModel
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

class Application : Application(), KoinComponent, Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        val viewModelModule = module {
            viewModel{
                MovieListViewModel(get() as NetworkMoviesRepositoryInterface)
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

            worker {UpcomingMoviesWorker(get(), get(), get() as NetworkMoviesRepositoryInterface)}
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