package com.merajavier.cineme.data.local

import com.merajavier.cineme.data.local.daos.UserSessionDao
import com.merajavier.cineme.data.local.entities.UserSessionEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface LocalAccountRepositoryInterface{

    suspend fun createSession(userSessionEntity: UserSessionEntity)
    suspend fun getSession(username: String) : UserSessionEntity?
    suspend fun deleteSession(userSessionEntity: UserSessionEntity)
}

class LocalAccountRepository(
    private val userSessionDao: UserSessionDao,
    private val iosDispatcher: CoroutineDispatcher = Dispatchers.IO
)
    : LocalAccountRepositoryInterface{

    override suspend fun createSession(userSessionEntity: UserSessionEntity) = withContext(iosDispatcher){
        userSessionDao.addSession(userSessionEntity)
    }

    override suspend fun getSession(username: String): UserSessionEntity? = withContext(iosDispatcher){
        return@withContext userSessionDao.getSession(username)
    }

    override suspend fun deleteSession(userSessionEntity: UserSessionEntity) = withContext(iosDispatcher) {
        userSessionDao.deleteSession(userSessionEntity)
    }
}