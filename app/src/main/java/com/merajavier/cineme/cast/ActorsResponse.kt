package com.merajavier.cineme.cast

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActorsResponse (
    val id: Int,
    val cast: List<ActorDataItem>,
    val crew: List<CrewDataItem>
) : Parcelable