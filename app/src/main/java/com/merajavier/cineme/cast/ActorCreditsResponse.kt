package com.merajavier.cineme.cast

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActorCreditsResponse(
    val cast: List<ActorCreditDataItem>
) : Parcelable