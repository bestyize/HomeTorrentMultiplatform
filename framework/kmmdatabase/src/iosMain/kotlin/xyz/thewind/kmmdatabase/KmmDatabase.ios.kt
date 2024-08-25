package xyz.thewind.kmmdatabase

import androidx.room.Room
import androidx.room.RoomDatabase

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual inline fun <reified T : RoomDatabase> getDatabase(name: String): T {
    val dbFilePath = documentDirectory() + "/$name.db"
    return Room.databaseBuilder<T>(
        name = dbFilePath,
    ).build()
}

fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}