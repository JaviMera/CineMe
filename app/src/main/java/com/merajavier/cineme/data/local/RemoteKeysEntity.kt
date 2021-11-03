package com.merajavier.cineme.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val repoId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)