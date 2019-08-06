package com.badap

import java.io.Serializable

data class Artist(val artistId: String,
                  val artistName: String,
                  val artistNumberOfTracks: String,
                  val artistNumberOfAlbums: String,
                  val artistIdLong: Long,
                  val firstAlbumArt: String?) : Serializable

data class Album(val albumId: String,
                  val albumName: String,
                  val artist: String,
                  val numOfSongs: String,
                  val albumArt: String?) : Serializable

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
                val albumArtUri: String?) : Serializable