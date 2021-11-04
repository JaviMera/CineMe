package com.merajavier.cineme.koin.modules

import com.merajavier.cineme.NowPlayingMoviesWorker
import com.merajavier.cineme.UpcomingMoviesWorker
import com.merajavier.cineme.data.local.TMDBDatabase
import com.merajavier.cineme.network.repositories.NetworkMoviesRepositoryInterface
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {

    worker { UpcomingMoviesWorker(get(), get(), get() as NetworkMoviesRepositoryInterface) }
    worker { NowPlayingMoviesWorker(get(), get(), get() as NetworkMoviesRepositoryInterface, get() as TMDBDatabase) }
}