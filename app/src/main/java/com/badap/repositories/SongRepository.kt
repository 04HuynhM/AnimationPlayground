package com.badap.repositories

import android.app.Application
import com.badap.SongEntity
import com.badap.database.MusicDatabase
import com.badap.database.SongDao

class SongRepository(application: Application) {

    private var songDao: SongDao
    private var allSongs: List<SongEntity>

    init {
        val database = MusicDatabase.invoke(application)
        songDao = database.songDao()
        allSongs = songDao.getAll()
    }



}