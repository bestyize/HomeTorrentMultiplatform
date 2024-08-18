package com.home.torrent.collect.database

import androidx.room.InvalidationTracker
import com.home.torrent.collect.database.bean.CollectTorrentInfo
import com.home.torrent.collect.database.dao.CollectTorrentDao
import com.home.torrent.collect.database.table.TorrentDatabase


internal val torrentDb by lazy {
    object : TorrentDatabase() {
        override fun collectDao(): CollectTorrentDao = object : CollectTorrentDao {
            override suspend fun loadCollectedTorrent(): List<CollectTorrentInfo> {
                return emptyList()
            }

            override suspend fun insert(data: CollectTorrentInfo): Long {
                return 1
            }

            override suspend fun deleteAll(): Int {
                return 1
            }

            override suspend fun deleteById(id: Int): Int {
                return 1
            }

            override suspend fun deleteByMagnetUrl(magnetUrl: String): Int {
                return 1
            }

            override suspend fun modifyTorrentTitle(newTitle: String, hash: String): Int {
                return 1
            }
        }

        override fun createInvalidationTracker(): InvalidationTracker {
            return InvalidationTracker(this, emptyMap(), emptyMap())
        }

    }
}