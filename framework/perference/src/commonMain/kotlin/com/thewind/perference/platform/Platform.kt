package com.thewind.perference.platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect fun getDefaultDatastore(): DataStore<Preferences>