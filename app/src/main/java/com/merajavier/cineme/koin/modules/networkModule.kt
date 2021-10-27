package com.merajavier.cineme.koin.modules

import com.merajavier.cineme.BuildConfig
import com.merajavier.cineme.network.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module{
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { provideTmdbMoviesApi(get()) as TMDBApiInterface }
    factory { provideTmdbCastApi(get())}
    factory { provideTmdbAuthenticationApi(get()) }
    factory { provideTmdbGuestApi(get())}
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
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

fun provideTmdbMoviesApi(retrofit: Retrofit) : TMDBApiInterface{
    return retrofit.create(TMDBApiInterface::class.java)
}

fun provideTmdbCastApi(retrofit: Retrofit) : TMDBApiCastInterface{
    return retrofit.create(TMDBApiCastInterface::class.java)
}

fun provideTmdbAuthenticationApi(retrofit: Retrofit) : TMDBApiLoginInterface{
    return retrofit.create(TMDBApiLoginInterface::class.java)
}

fun provideTmdbGuestApi(retrofit: Retrofit) : TMDBApiGuestSessionInterface{
    return retrofit.create(TMDBApiGuestSessionInterface::class.java)
}