package com.badap.repositories

import android.app.Application
import android.util.Log
import com.badap.AlbumEntity
import com.badap.database.AlbumDao
import com.badap.database.MusicDatabase
import io.reactivex.Observable

class AlbumRepository(application: Application) {

    private val TAG = "ALBUM_REPO"
    private var albumDao: AlbumDao

    init {
        val database = MusicDatabase.getInstance(application)
        albumDao = database.albumDao()
    }

    fun getAllAlbumsAsync(): Observable<List<AlbumEntity>> {
        return albumDao.getAll()
                       .filter { it.isNotEmpty() }
                       .toObservable()
                       .doOnNext {
                           Log.i(TAG, "Album repo, get all albums from DB. Size: ${it.size}")
                       }
    }

    fun getAllAlbumsForArtist(artistId: Long): Observable<List<AlbumEntity>> {
        return albumDao.getAlbumsForArtist(artistId)
                       .filter { it.isNotEmpty() }
                       .toObservable()
                       .doOnNext {
                           Log.i(TAG, "Album repo, get all albums for artist $artistId from db. Size: ${it.size}")
                       }
    }


}