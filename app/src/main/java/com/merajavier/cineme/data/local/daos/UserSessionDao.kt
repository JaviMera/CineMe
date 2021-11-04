package com.merajavier.cineme.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.merajavier.cineme.data.local.entities.UserSessionEntity

@Dao
interface UserSessionDao{
    @Query("SELECT * from user_session where user_name = :userName")
    fun getSession(userName: String) : UserSessionEntity?

    @Insert(onConflict = REPLACE)
    suspend fun addSession(userSessionEntity: UserSessionEntity)

    @Delete(entity = UserSessionEntity::class)
    suspend fun deleteSession(userSessionEntity: UserSessionEntity)
}