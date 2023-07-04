package com.spark.fastplayer.data.pefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("content_play_history")
        private val CHANNEL_ID_KEY = stringPreferencesKey("channel_id")
        private val TAXONOMY_ID_KEY = stringPreferencesKey("taxonomy_id")
        private val PROGRAM_ID_KEY = stringPreferencesKey("program_id")
    }

    val getChannelId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CHANNEL_ID_KEY] ?: ""
    }
    val getTaxonomyId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TAXONOMY_ID_KEY] ?: ""
    }
    val getProgramId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PROGRAM_ID_KEY] ?: ""
    }

    suspend fun saveTaxonomyId(taxonomyId: String) {
        context.dataStore.edit { preferences ->
            preferences[TAXONOMY_ID_KEY] = taxonomyId
        }
    }
    suspend fun saveChannelId(channelId: String) {
        context.dataStore.edit { preferences ->
            preferences[CHANNEL_ID_KEY] = channelId
        }
    }

    suspend fun saveProgramId(programId: String) {
        context.dataStore.edit { preferences ->
            preferences[PROGRAM_ID_KEY] = programId
        }
    }
}