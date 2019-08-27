package com.badap.database

import androidx.room.*
import com.badap.ArtistEntity
import io.reactivex.Flowable

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artists_table ORDER BY name ASC")
    fun getAll() : List<ArtistEntity>

    @Insert
    fun addArtist(artist: ArtistEntity)

    @Delete
    fun deleteArtist(artist: ArtistEntity)

    @Update
    fun updateArtist(alartistbum: ArtistEntity)

}