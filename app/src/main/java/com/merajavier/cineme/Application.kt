package com.merajavier.cineme

import android.app.Application
import android.content.SharedPreferences
import com.merajavier.cineme.cast.CastListViewModel
import com.merajavier.cineme.data.local.*
import com.merajavier.cineme.koin.modules.networkModule
import com.merajavier.cineme.koin.modules.sharedPreferencesModule
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.login.account.AccountViewModel
import com.merajavier.cineme.login.account.UserFragmentDirections
import com.merajavier.cineme.movies.MovieListViewModel
import com.merajavier.cineme.network.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import timber.log.Timber

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val viewModelModule = module {
            viewModel{
                MovieListViewModel(get() as NetworkMovieRepository)
            }

            viewModel{
                CastListViewModel(get() as NetworkMovieActorRepository)
            }

            viewModel () {
                LoginViewModel(
                    get() as NetworkLoginRepositoryInterface,
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

            single {
                NetworkAuthenticationRepository(get() as TMDBApiAuthenticationInterface) as NetworkAuthenticationRepositoryInterface
            }

            single{
                NetworkMovieRepository(get() as TMDBApiInterface)
            }

            single{
                NetworkMovieActorRepository(get() as TMDBApiCastInterface)
            }

            single{
                NetworkLoginRepository(get() as TMDBApiLoginInterface) as NetworkLoginRepositoryInterface
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
        }

        startKoin {
            androidContext(this@Application)
            modules(viewModelModule, networkModule, sharedPreferencesModule)
        }

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}