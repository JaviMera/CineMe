package com.merajavier.cineme.cast

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActorProfileImageDataItem(
    @SerializedName("file_path") val filePath: String,
    @SerializedName("vote_count") val VoteCount: Int
) : Parcelable