package com.merajavier.cineme

import android.app.Application
import com.merajavier.cineme.cast.CastListViewModel
import com.merajavier.cineme.koin.modules.networkModule
import com.merajavier.cineme.login.LoginViewModel
import com.merajavier.cineme.login.account.AccountViewModel
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
                    get() as NetworkAuthenticationRepositoryInterface)
            }

            viewModel {
                AccountViewModel(
                    get() as NetworkAccountRepositoryInterface
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