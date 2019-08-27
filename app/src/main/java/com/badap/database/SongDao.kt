package com.badap.database

import androidx.room.*
import com.badap.SongEntity
import io.reactivex.Single

@Dao
interface SongDao {

    @Query("SELECT * FROM songs_table ORDER BY title ASC")
    fun getAll() : Single<List<SongEntity>>

    @Query("SELECT * FROM songs_table WHERE artistId LIKE :artistId ORDER BY title")
    fun getSongsForArtist(artistId: Long): Single<List<SongEntity>>

    @Query("SELECT * FROM songs_table WHERE albumId LIKE :albumId ORDER BY trackNumber")
    fun getSongsForAlbum(albumId: Long): Single<List<SongEntity>>

    @Insert
    fun addSong(song: SongEntity)

    @Delete
    fun deleteSong(song: SongEntity)

    @Update
    fun updateSong(song: SongEntity)


}