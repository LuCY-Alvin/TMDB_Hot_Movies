package exercise.movieintmdb

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.preferencesDataStore

object SessionDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_data")

    val SESSION_ID_KEY = stringPreferencesKey("session_id")
    val ACCOUNT_ID_KEY = stringPreferencesKey("account_id")

    suspend fun storeSessionData(context: Context, sessionId: String, accountId: String) {
        context.dataStore.edit { preferences ->
            preferences[SESSION_ID_KEY] = sessionId
            preferences[ACCOUNT_ID_KEY] = accountId
        }
    }

    suspend fun getSessionData(context: Context): Pair<String?, String?> {
        val preferences = context.dataStore.data.first()
        return Pair(preferences[SESSION_ID_KEY], preferences[ACCOUNT_ID_KEY])
    }
}