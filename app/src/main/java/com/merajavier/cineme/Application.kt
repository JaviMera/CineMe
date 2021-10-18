package com.merajavier.cineme

import android.app.Application
import com.merajavier.cineme.movies.MoviesViewModel
import com.merajavier.cineme.network.AuthInterceptor
import com.merajavier.cineme.network.TMDBApiInterface
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val viewModelModule = module {
            viewModel{
                MoviesViewModel(get() as TMDBApiInterface)
            }
        }

        val networkModule = module{
            factory { AuthInterceptor() }
            factory { provideOkHttpClient(get()) }
            factory { provideTmdbApi(get()) as TMDBApiInterface }
            single{provideMoshi()}
            single{provideRetrofit(get(), get())}
        }

        startKoin {
            androidContext(this@Application)
            modules(viewModelModule, networkModule)
        }
    }

    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
    }

    fun provideMoshi() : Moshi{
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
}