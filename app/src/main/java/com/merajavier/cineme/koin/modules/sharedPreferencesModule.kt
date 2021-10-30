package com.merajavier.cineme.koin.modules

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPreferencesModule = module {
    single {
        provideLoginPreferences(androidContext())
    }
}

private const val LOGIN_PREFERENCES_KEY = "LOGIN_PREFERENCES"

private fun provideLoginPreferences(context: Context) : SharedPreferences =
    context.getSharedPreferences(LOGIN_PREFERENCES_KEY, MODE_PRIVATE)