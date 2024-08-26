package com.home.torrent.collect.database.table

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.home.torrent.collect.database.bean.CollectTorrentInfo
import com.home.torrent.collect.database.dao.CollectTorrentDao

@Database(entities = [CollectTorrentInfo::class], version = 1)
@ConstructedBy(value = TorrentDatabaseConstructor::class)
abstract class TorrentDatabase : RoomDatabase() {
    abstract fun collectDao(): CollectTorrentDao
}
