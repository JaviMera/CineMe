package com.merajavier.cineme.cast

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActorProfileImagesResponse(
    val profiles: List<ActorProfileImageDataItem>?
) : Parcelable