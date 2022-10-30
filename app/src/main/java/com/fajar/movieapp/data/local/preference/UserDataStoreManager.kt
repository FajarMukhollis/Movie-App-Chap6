package com.fajar.movieapp.data.local.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun setUser(user: UserPreferences) {
        context.userDataStore.edit { preferences ->
            preferences[ID_KEY] = user.id ?: 0
            preferences[USERNAME_KEY] = user.username
            preferences[EMAIL_KEY] = user.email
            preferences[PASSWORD_KEY] = user.password ?: ""
        }
    }

    suspend fun updateUser(user: UserPreferences) {
        context.userDataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
            preferences[EMAIL_KEY] = user.email
            preferences[FULL_NAME_KEY] = user.fullName ?: ""
            preferences[DATE_OF_BIRTH_KEY] = user.dateOfBirth ?: ""
            preferences[ADDRESS_KEY] = user.address ?: ""
        }
    }

    suspend fun setUserLogin(isLogin: Boolean) {
        context.userDataStore.edit { preferences ->
            preferences[LOGIN_STATUS_KEY] = isLogin
        }
    }

    suspend fun setProfileImage(image: String) {
        context.userDataStore.edit { preferences ->
            preferences[PROFILE_IMAGE_KEY] = image
        }
    }

    fun getUser(): Flow<UserPreferences> {
        return context.userDataStore.data.map { preferences ->
            UserPreferences(
                preferences[ID_KEY] ?: 0,
                preferences[USERNAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[FULL_NAME_KEY] ?: "",
                preferences[DATE_OF_BIRTH_KEY] ?: "",
                preferences[ADDRESS_KEY] ?: "",
                preferences[PROFILE_IMAGE_KEY] ?: "",
                preferences[LOGIN_STATUS_KEY] ?: false
            )
        }
    }

    fun getUserLogin(): Flow<Boolean> {
        return context.userDataStore.data.map { preferences ->
            preferences[LOGIN_STATUS_KEY] ?: false
        }
    }

    companion object {
        private const val DATA_STORE_NAME = "user_preferences"
        private val ID_KEY = intPreferencesKey("id_key")
        private val USERNAME_KEY = stringPreferencesKey("name_key")
        private val EMAIL_KEY = stringPreferencesKey("email_key")
        private val PASSWORD_KEY = stringPreferencesKey("password_key")
        private val FULL_NAME_KEY = stringPreferencesKey("full_name_key")
        private val DATE_OF_BIRTH_KEY = stringPreferencesKey("date_of_birth_key")
        private val ADDRESS_KEY = stringPreferencesKey("address_key")
        private val PROFILE_IMAGE_KEY = stringPreferencesKey("profile_image_key")
        private val LOGIN_STATUS_KEY = booleanPreferencesKey("login_status_key")

        val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
    }
}