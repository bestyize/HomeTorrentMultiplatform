package com.thewind.perference.platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.home.baseapp.app.HomeApp
import okio.Path.Companion.toPath

private val dataStore by lazy {
    PreferenceDataStoreFactory.createWithPath(produceFile = {
        HomeApp.context.filesDir.resolve(dataStoreFileName).absolutePath.toPath()
    })
}

actual fun getDefaultDatastore(): DataStore<Preferences> = dataStore

private const val dataStoreFileName = "config.preferences_pb"