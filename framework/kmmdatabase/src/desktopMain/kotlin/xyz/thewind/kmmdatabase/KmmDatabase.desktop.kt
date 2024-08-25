package xyz.thewind.kmmdatabase

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import java.io.File

val USER_DIR by lazy { System.getProperty("user.home") + File.separator + "com.thewind.torrent" + File.separator + "db" }

actual inline fun <reified T : RoomDatabase> getDatabase(name: String): T {
    return Room.databaseBuilder<T>(
        name = "$USER_DIR${File.separator + name}.db",
    ).fallbackToDestructiveMigrationOnDowngrade(true).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO).build()
}