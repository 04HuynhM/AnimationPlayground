package com.badap.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.badap.AlbumEntity
import com.badap.repositories.AlbumRepository
import io.reactivex.Observable

class AlbumViewModel(application: Application, artistId: Long?) : AndroidViewModel(application) {

    private var albumRepository: AlbumRepository = AlbumRepository(application)
    private var albumList: Observable<List<AlbumEntity>>

    init {
        albumList = if (artistId != null) {
            albumRepository.getAllAlbumsForArtist(artistId)
        } else {
            albumRepository.getAllAlbumsAsync()
        }
    }

    fun getAlbumList(): Observable<List<AlbumEntity>> {
        return albumList
    }
}