package com.thewind.perference.platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import java.io.File


private val dataStore by lazy {
    PreferenceDataStoreFactory.createWithPath(produceFile = {
        (System.getProperty("user.home") + File.separator + "com.thewind.torrent" + File.separator + "config.preferences_pb").toPath()
    })
}


actual fun getDefaultDatastore(): DataStore<Preferences> = dataStore