package com.merajavier.cineme

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.work.*
import com.merajavier.cineme.data.local.*
import com.merajavier.cineme.koin.modules.*
import com.merajavier.cineme.network.*
import com.merajavier.cineme.network.repositories.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.workmanager.dsl.worker
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber

@ExperimentalPagingApi
class Application : Application(), KoinComponent, Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(
                networkModule,
                sharedPreferencesModule,
                workerModule,
                repositoriesModule,
                viewModelsModule,
                databaseModule(this@Application)
            )
            workManagerFactory()
        }

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    // Configure a custom initialization of WorkManager since workers are injected through Koin
    // and cannot depend on the default WorkManager initialized in androidmanifest.xml
    override fun getWorkManagerConfiguration(): Configuration {

        val config = Configuration.Builder()
            .build()

        WorkManager.initialize(this, config)
        return config
    }
}