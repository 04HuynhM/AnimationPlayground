package com.badap

import com.google.gson.Gson
import java.io.Serializable

data class Artist(val artistId: String,
                  val artistName: String,
                  val artistNumberOfTracks: String,
                  val artistNumberOfAlbums: String,
                  val artistIdLong: Long,
                  val firstAlbumArt: String?) : Serializable {

    fun toJson() : String {
        val jsonString = Gson().toJson(this)
        return jsonString.toString()
    }
}

data class Album(val albumId: String,
                  val albumName: String,
                  val artist: String,
                  val numOfSongs: String,
                  val albumArt: String?) : Serializable {

    fun toJson() : String {
        val jsonString = Gson().toJson(this)
        return jsonString.toString()
    }
}

data class Song(val songId: Long,
                val name: String,
                val title: String,
                val trackNumber: String,
                val album: String,
                val albumId: Long,
                val artist: String,
                val artistId: String,
                val path: String,
                val duration: String,
                val rawDuration: String,
                val albumArtUri: String?) : Serializable {

    fun toJson() : String {
        val jsonString = Gson().toJson(this)
        return jsonString.toString()
    }
}