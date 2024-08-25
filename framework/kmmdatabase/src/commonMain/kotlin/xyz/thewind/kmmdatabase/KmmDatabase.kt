package xyz.thewind.kmmdatabase

import androidx.room.RoomDatabase


expect inline fun <reified T : RoomDatabase> getDatabase(name: String): T
