package com.merajavier.cineme.koin.modules

import android.content.Context
import com.merajavier.cineme.data.local.*
import com.merajavier.cineme.data.local.daos.UserSessionDao
import org.koin.core.module.Module
import org.koin.dsl.module

fun databaseModule(context: Context) : Module = module {
    single{
        LocalAccountRepository(
            get() as UserSessionDao
        ) as LocalAccountRepositoryInterface
    }

    single {
        TMDBDatabase.getInstance(context).userSessionDao
    }

    single {
        TMDBDatabase.getInstance(context).nowPlayingMoviesDao
    }

    single {
        TMDBDatabase.getInstance(context).remoteKeysDao
    }

    single {
        TMDBDatabase.getInstance(context) as TMDBDatabase
    }
}