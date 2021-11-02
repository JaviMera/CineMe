package com.merajavier.cineme.koin.modules

import com.merajavier.cineme.BuildConfig
import com.merajavier.cineme.network.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val networkModule = module{
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { provideTmdbMoviesApi(get()) as TMDBApMoviesiInterface }
    factory { provideTmdbCastApi(get())}
    factory {provideTmdbAuthenticationApi(get())}
    factory{ provideTmdbAccountApi(get())}
    factory { provideTmdbSearchApi(get()) }
    single{provideMoshi()}
    single{provideRetrofit(get(), get())}
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
}

fun provideMoshi() : Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient) : Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideTmdbMoviesApi(retrofit: Retrofit) : TMDBApMoviesiInterface{
    return retrofit.create(TMDBApMoviesiInterface::class.java)
}

fun provideTmdbCastApi(retrofit: Retrofit) : TMDBApiCastInterface{
    return retrofit.create(TMDBApiCastInterface::class.java)
}

fun provideTmdbAuthenticationApi(retrofit: Retrofit) : TMDBApiAuthenticationInterface {
    return retrofit.create(TMDBApiAuthenticationInterface::class.java)
}

fun provideTmdbAccountApi(retrofit: Retrofit) : TMDBApiAccountInterface {
    return retrofit.create(TMDBApiAccountInterface::class.java)
}

fun provideTmdbSearchApi(retrofit: Retrofit) : TMDBApiSearchInterface {
    return retrofit.create(TMDBApiSearchInterface::class.java)
}
