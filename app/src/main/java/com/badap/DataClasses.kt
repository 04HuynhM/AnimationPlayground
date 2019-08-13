package com.badap

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.Gson
import java.io.Serializable

@Entity(tableName = "artists_table")
data class ArtistEntity(@PrimaryKey val artistId: Long,
                        val name: String,
                        val numberOfTracks: String,
                        val numberOfAlbums: String,
                        val artistIdLong: Long,
                        val firstAlbumArt: String?) : Serializable {

    fun toJson() : String {
        val jsonString = Gson().toJson(this)
        return jsonString.toString()
    }
}

@Entity(tableName = "albums_table",
        foreignKeys = [ForeignKey(entity = ArtistEntity::class,
                                  parentColumns = arrayOf("artistId"),
                                  childColumns = arrayOf("artistId"),
                                  onDelete = ForeignKey.CASCADE)])
data class AlbumEntity(@PrimaryKey val albumId: Long,
                       val name: String,
                       val artist: String,
                       val artistId: Long,
                       val numOfSongs: Int,
                       val albumArtUriString: String?) : Serializable {

    fun toJson() : String {
        val jsonString = Gson().toJson(this)
        return jsonString.toString()
    }
}

@Entity(tableName = "songs_table",
        foreignKeys = arrayOf(ForeignKey(entity = AlbumEntity::class,
                                  parentColumns = arrayOf("albumId"),
                                  childColumns = arrayOf("albumId"),
                                  onDelete = ForeignKey.CASCADE),
                              ForeignKey(entity = ArtistEntity::class,
                                  parentColumns = arrayOf("artistId"),
                                  childColumns = arrayOf("artistId"),
                                  onDelete = ForeignKey.CASCADE)))
data class SongEntity(@PrimaryKey val songId: Long,
                      val name: String,
                      val title: String,
                      val trackNumber: String,
                      val album: String,
                      val albumId: Long,
                      val artist: String,
                      val artistId: Long,
                      val path: String,
                      val duration: String,
                      val rawDuration: String,
                      val albumArtUriString: String?) : Serializable {

    fun toJson() : String {
        val jsonString = Gson().toJson(this)
        return jsonString.toString()
    }
}