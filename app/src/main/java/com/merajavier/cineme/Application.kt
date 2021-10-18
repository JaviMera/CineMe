package com.merajavier.cineme

import android.app.Application
import com.merajavier.cineme.movies.MoviesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val viewModelModule = module {
            viewModel{
                MoviesViewModel(get())
            }
        }

        startKoin {
            androidContext(this@Application)
            modules(viewModelModule)
        }
    }
}