package com.merajavier.cineme.koin.modules

import com.merajavier.cineme.BuildConfig
import com.merajavier.cineme.network.AuthInterceptor
import com.merajavier.cineme.network.TMDBApiInterface
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module{
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { provideTmdbApi(get()) as TMDBApiInterface }
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

fun provideTmdbApi(retrofit: Retrofit) : TMDBApiInterface{
    return retrofit.create(TMDBApiInterface::class.java)
}