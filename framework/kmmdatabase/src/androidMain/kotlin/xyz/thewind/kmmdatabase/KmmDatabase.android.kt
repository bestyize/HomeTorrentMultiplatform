package xyz.thewind.kmmdatabase

import androidx.room.Room
import androidx.room.RoomDatabase
import com.home.baseapp.app.HomeApp

actual inline fun <reified T : RoomDatabase> getDatabase(name: String): T{
    val appContext = HomeApp.context
    val dbFile = appContext.getDatabasePath("$name.db")
    return Room.databaseBuilder<T>(
        context = appContext,
        name = dbFile.absolutePath
    ).build()
}