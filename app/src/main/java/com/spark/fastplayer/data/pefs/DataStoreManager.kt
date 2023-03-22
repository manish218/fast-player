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
        private val CHANNEL_ID_KEY = stringPreferencesKey("c_id")
        private val TAXONOMY_ID_KEY = stringPreferencesKey("t_id")
    }

    val getChannelId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CHANNEL_ID_KEY] ?: ""
    }
    val getTaxonomyId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TAXONOMY_ID_KEY] ?: ""
    }

    suspend fun saveTaxonomyId(taxID: String) {
        context.dataStore.edit { preferences ->
            preferences[TAXONOMY_ID_KEY] = taxID
        }
    }
    suspend fun saveChannelId(channelID: String) {
        context.dataStore.edit { preferences ->
            preferences[CHANNEL_ID_KEY] = channelID
        }
    }
}