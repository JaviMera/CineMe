package com.merajavier.cineme.login.authentication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeleteSessionResponse(
    val success: Boolean
) : Parcelable