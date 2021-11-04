package com.merajavier.cineme.koin.modules

import com.merajavier.cineme.network.*
import com.merajavier.cineme.network.repositories.*
import org.koin.dsl.module


val repositoriesModule = module {
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
}