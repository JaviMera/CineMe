package com.merajavier.cineme.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val repoId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)