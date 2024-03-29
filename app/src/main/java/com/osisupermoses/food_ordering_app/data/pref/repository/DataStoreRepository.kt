package com.osisupermoses.food_ordering_app.data.pref.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.osisupermoses.food_ordering_app.data.pref.PreferencesKeys
import com.osisupermoses.food_ordering_app.domain.model.RecipesItem
import com.osisupermoses.food_ordering_app.util.getPathFromUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")

class DataStoreRepository(context: Context) {

    private val dataStore = context.dataStore
    private val context = context.applicationContext

    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.onBoardingKey] = completed
        }
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException)
                    emit(emptyPreferences())
                else
                    throw exception
            }
            .map { preferences ->
                val onBoardingState = preferences[PreferencesKeys.onBoardingKey] ?: false
                onBoardingState
            }
    }

    suspend fun save(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun read(key: Preferences.Key<String>): Flow<String> = dataStore.data
        .map { preferences ->
            preferences[key] ?: ""
        }

    fun getImageUri(uri: Uri): String? {
        return getPathFromUri(context, uri)
    }

    fun getRecipeDetailsImages(recipesItem: RecipesItem): List<String> {
        val imageList = mutableListOf<String>()
        recipesItem.images?.forEach {
            val i = getPathFromUri(context, it.toUri())
            if (i != null) {
                imageList.add(i)
            }
        }
        return imageList
    }
}