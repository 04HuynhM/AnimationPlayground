package com.badap.repositories

import android.app.Application
import android.util.Log
import com.badap.ArtistEntity
import com.badap.database.ArtistDao
import com.badap.database.MusicDatabase
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ArtistRepository(application: Application) {

    private val TAG = "ARTIST_REPO"
    private var artistDao: ArtistDao
    private var artistList: List<ArtistEntity>

    init {
        val database = MusicDatabase.getInstance(application)
        artistDao = database.artistDao()
        artistList = artistDao.getAll()
    }

    fun getAllArtists(): List<ArtistEntity> {
        return artistList
    }
}