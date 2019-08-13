package com.badap.repositories

import android.app.Application
import com.badap.ArtistEntity
import com.badap.database.ArtistDao
import com.badap.database.MusicDatabase

class ArtistRepository(application: Application) {

    private var artistDao: ArtistDao
    private var allSongs: List<ArtistEntity>

    init {
        val database = MusicDatabase.invoke(application)
        artistDao = database.artistDao()
        allSongs = artistDao.getAll()
    }



}