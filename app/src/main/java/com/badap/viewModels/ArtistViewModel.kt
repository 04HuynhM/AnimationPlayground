package com.badap.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.badap.ArtistEntity
import com.badap.repositories.ArtistRepository
import io.reactivex.Flowable
import io.reactivex.Single

class ArtistViewModel(application: Application) : AndroidViewModel(application) {

    private var artistRepository: ArtistRepository = ArtistRepository(application)

    fun getArtistList(): List<ArtistEntity> {
        return artistRepository.getAllArtists()
    }

}