package com.badap.repositories

import android.app.Application
import android.util.Log
import com.badap.SongEntity
import com.badap.database.MusicDatabase
import com.badap.database.SongDao
import io.reactivex.Observable

class SongRepository(application: Application) {

    private val TAG = "SONG_REPO"
    private var songDao: SongDao

    init {
        val database = MusicDatabase.getInstance(application)
        songDao = database.songDao()
    }

    fun getAllSongs(): Observable<List<SongEntity>> {
        return songDao.getAll()
            .filter { it.isNotEmpty() }
            .toObservable()
            .doOnNext {
                Log.i(TAG, "Artist repo, get all artists. Size: ${it.size}")
            }
    }

    fun getAllSongsForArtist(artistId: Long): Observable<List<SongEntity>> {
        return songDao.getSongsForArtist(artistId)
            .filter { it.isNotEmpty() }
            .toObservable()
            .doOnNext {
                Log.i(TAG, "Song repo, get all songs for artist $artistId. Size: ${it.size}")
            }
    }

    fun getAllSongsForAlbum(albumId: Long): Observable<List<SongEntity>> {
        return songDao.getSongsForAlbum(albumId)
            .filter { it.isNotEmpty() }
            .toObservable()
            .doOnNext {
                Log.i(TAG, "Song repo, get all songs for artist $albumId. Size: ${it.size}")
            }
    }



}