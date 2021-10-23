package com.merajavier.cineme

import android.app.Application
import com.merajavier.cineme.koin.modules.networkModule
import com.merajavier.cineme.movies.MovieListViewModel
import com.merajavier.cineme.network.NetworkMovieRepository
import com.merajavier.cineme.network.TMDBApiInterface
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

            single{
                NetworkMovieRepository(get() as TMDBApiInterface)
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