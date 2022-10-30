package com.fajar.movieapp.data.repository

import com.fajar.movieapp.data.local.preference.UserPreferenceDataSource
import com.fajar.movieapp.data.local.preference.UserPreferences
import com.fajar.movieapp.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserRepository {
    suspend fun setUser(user: UserPreferences)
    suspend fun updateUser(user: UserPreferences)
    suspend fun setUserLogin(isLogin: Boolean)
    suspend fun setProfileImage(image: String)

    fun getUser(): Flow<UserPreferences>
    fun getUserLogin(): Flow<Boolean>
}

class UserRepositoryImpl @Inject constructor(
    private val userPreferenceDataSource: UserPreferenceDataSource,
): UserRepository {
    override suspend fun setUser(user: UserPreferences) {
        userPreferenceDataSource.setUser(user)
    }

    override suspend fun updateUser(user: UserPreferences) {
        userPreferenceDataSource.updateUser(user)
    }

    override suspend fun setUserLogin(isLogin: Boolean) {
        userPreferenceDataSource.setUserLogin(isLogin)
    }

    override suspend fun setProfileImage(image: String) {
        userPreferenceDataSource.setProfileImage(image)
    }

    override fun getUser(): Flow<UserPreferences> {
        return userPreferenceDataSource.getUser()
    }

    override fun getUserLogin(): Flow<Boolean> {
        return userPreferenceDataSource.getUserLogin()
    }

    private suspend fun <T> proceed(coroutine: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(coroutine.invoke())
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

}