package com.badap.database

import androidx.room.*
import com.badap.AlbumEntity

@Dao
interface AlbumDao {

    @Query("SELECT * FROM albums_table ORDER BY name ASC")
    fun getAll() : List<AlbumEntity>

    @Query("SELECT * FROM albums_table WHERE artistId LIKE :artistId ORDER BY name ASC")
    fun getAlbumsForArtist(artistId: String) : List<AlbumEntity>

    @Insert
    fun addAlbum(album: AlbumEntity)

    @Delete
    fun deleteAlbum(album: AlbumEntity)

    @Update
    fun updateAlbum(album: AlbumEntity)
}