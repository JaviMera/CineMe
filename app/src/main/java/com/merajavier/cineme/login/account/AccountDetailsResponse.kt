package com.merajavier.cineme.login.account

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class AccountDetailsResponse(
    val id: Int,
    val username: String
) : Parcelable