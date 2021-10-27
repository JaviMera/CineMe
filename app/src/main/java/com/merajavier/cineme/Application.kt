package com.merajavier.cineme

import android.app.Application
import com.merajavier.cineme.cast.CastListViewModel
import com.merajavier.cineme.koin.modules.networkModule
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MovieListViewModel
import com.merajavier.cineme.account.guest.GuestViewModel
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

            viewModel {
                LoginViewModel(get() as NetworkLoginRepositoryInterface)
            }

            viewModel {
                GuestViewModel(get() as NetworkGuestRepositoryInterface)
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

            single {
                NetworkGuestRepository(get() as TMDBApiGuestSessionInterface) as NetworkGuestRepositoryInterface
            }
        }

        startKoin {
            androidContext(this@Application)
            modules(viewModelModule, networkModule)
        }

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}