package com.badap.database

import androidx.room.*
import com.badap.SongEntity

@Dao
interface SongDao {

    @Query("SELECT * FROM songs_table ORDER BY title ASC")
    fun getAll() : List<SongEntity>

    @Query("SELECT * FROM songs_table WHERE artistId LIKE :songId ORDER BY title")
    fun getSongsForArtist(songId: String): List<SongEntity>

    @Query("SELECT * FROM songs_table WHERE albumId LIKE :songId ORDER BY trackNumber")
    fun getSongsForAlbum(songId: String): List<SongEntity>

    @Insert
    fun addSong(song: SongEntity)

    @Delete
    fun deleteSong(song: SongEntity)

    @Update
    fun updateSong(song: SongEntity)


}