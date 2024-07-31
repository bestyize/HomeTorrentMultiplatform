package com.thewind.widget.theme

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.thewind.perference.platform.getDefaultDatastore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal val DATA_STORE_KEY_THEME_ID = intPreferencesKey("data_store_key_theme_id")


object ThemeManager {

    val themeFlow: Flow<ThemeId> = getDefaultDatastore().data.map { pref ->
        val local = pref[DATA_STORE_KEY_THEME_ID] ?: 0
        ThemeId.entries.find { it.value == local } ?: ThemeId.AUTO
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateTheme(id: ThemeId) {
        GlobalScope.launch {
            getDefaultDatastore().edit {
                it[DATA_STORE_KEY_THEME_ID] = id.value
            }
        }

    }

}

enum class ThemeId(val value: Int) {
    AUTO(0), DAY(1), NIGHT(2)
}

