package bangkit.product.chickmed.data.local.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import bangkit.product.chickmed.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val id = intPreferencesKey("id")
    private val token = stringPreferencesKey("token")
    private val email = stringPreferencesKey("email")
    private val profile = stringPreferencesKey("profile")
    private val name = stringPreferencesKey("name")

    suspend fun saveUser(user: UserModel) {
        dataStore.edit {
            it[id] = user.id
            it[name] = user.name
            it[email] = user.email
            it[profile] = if (user.profile.isNullOrBlank()) "https://www.its.ac.id/international/wp-content/uploads/sites/66/2020/02/blank-profile-picture-973460_1280-1.jpg" else user.profile
            it[token] = if (user.token.contains("Bearer")) user.token else "Bearer ${user.token}"
        }
    }

    fun getUser(): Flow<UserModel> = dataStore.data.map {
        UserModel(
            it[id] ?: 0,
            it[name] ?: "",
            it[email] ?: "",
            it[profile] ?: "",
            it[token] ?: "",
        )
    }

    suspend fun destroyUser() = dataStore.edit { it.clear() }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}