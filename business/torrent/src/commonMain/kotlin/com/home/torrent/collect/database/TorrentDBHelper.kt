package com.home.torrent.collect.database

import com.home.torrent.collect.database.table.TorrentDatabase
import xyz.thewind.kmmdatabase.getDatabase


internal val torrentDb by lazy {
    getDatabase<TorrentDatabase>("torrent")
}