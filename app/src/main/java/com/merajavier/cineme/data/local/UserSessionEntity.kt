package com.merajavier.cineme.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_session")
data class UserSessionEntity(
    @PrimaryKey @ColumnInfo(name="user_name") var userName: String,
    @ColumnInfo(name="session_id") var sessionId: String,
    @ColumnInfo(name="account_id") var accountId: Int
)