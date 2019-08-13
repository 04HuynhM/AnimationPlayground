package com.badap.repositories

import android.app.Application
import com.badap.AlbumEntity
import com.badap.database.AlbumDao
import com.badap.database.MusicDatabase

class AlbumRepository(application: Application) {

    private var albumDao: AlbumDao
    private val allAlbums: List<AlbumEntity>

    init {
        val database = MusicDatabase.invoke(application)
        albumDao = database.albumDao()
        allAlbums = albumDao.getAll()
    }



}