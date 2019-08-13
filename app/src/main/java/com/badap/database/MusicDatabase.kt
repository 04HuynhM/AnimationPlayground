package com.badap.database

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.badap.AlbumEntity
import com.badap.ArtistEntity
import com.badap.SongEntity
import android.provider.MediaStore
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


@Database(
    entities = [ArtistEntity::class,
                AlbumEntity::class,
                SongEntity::class],
    version = 1
)
abstract class MusicDatabase: RoomDatabase() {

    abstract fun artistDao(): ArtistDao
    abstract fun albumDao(): AlbumDao
    abstract fun songDao(): SongDao

    companion object {
        @Volatile private var instance: MusicDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, MusicDatabase::class.java, "indexed_library")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        //Todo: Populate db here by async calling mediastore functions
                    }
                })
                .build()
    }
}

private class MediaStoreOperations {

    fun getAllSongs(context: Context): List<SongEntity> {
        val resolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor = resolver.query(uri, null, selection, null, sortOrder)

        val count: Int
        val songList = ArrayList<SongEntity>()

        if (cursor != null) {
            count = cursor.count

            if (count > 0) {
                while (cursor.moveToNext()) {
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                    val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                    val displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val trackNumber = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK))
                    val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val duration =
                        formatDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)))
                    val rawDuration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val songId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumArtUriString = getAlbumArtUri(albumId).toString()
                    songList.add(
                        SongEntity(
                            songId,
                            displayName,
                            title,
                            trackNumber,
                            album,
                            albumId,
                            artist,
                            artistId,
                            path,
                            duration,
                            rawDuration,
                            albumArtUriString
                        )
                    )
                }
            }
            cursor.close()
        }
        return songList
    }

    fun getAllArtists(context: Context): List<ArtistEntity> {
        val resolver = context.contentResolver
        val uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
        )
        val sortOrder = MediaStore.Audio.Artists.ARTIST + " ASC"
        val cursor = resolver.query(uri, projection, null, null, sortOrder)
        val artistList = ArrayList<ArtistEntity>()

        val count: Int

        if (cursor != null) {
            count = cursor.count

            if (count > 0) {
                while (cursor.moveToNext()) {
                    val artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID))
                    val artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))
                    val numOfTracks =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))
                    val numOfAlbums =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))
                    val artistIdLong = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID))
                    val albumArtUri = getFirstAlbumArtUri(context, artistIdLong)

                    artistList.add(
                        ArtistEntity(
                            artistId,
                            artistName,
                            numOfTracks,
                            numOfAlbums,
                            artistIdLong,
                            albumArtUri.toString()
                        )
                    )
                }
            }
            cursor.close()
        }
        return artistList
    }

    fun getAllAlbums(context: Context): List<AlbumEntity> {
        val resolver = context.contentResolver
        val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.ALBUM_ART
        )
        val sortOrder = MediaStore.Audio.Albums.ALBUM + " ASC"
        val cursor = resolver.query(uri, projection, null, null, sortOrder)
        val albumList = ArrayList<AlbumEntity>()

        val count: Int

        if (cursor != null) {
            count = cursor.count

            if (count > 0) {
                while (cursor.moveToNext()) {
                    val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
                    val albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                    val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
                    val numOfSongs = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
                    val albumArt = getAlbumArtUri(albumId)

                    val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    val songSelection = MediaStore.Audio.Media.ALBUM_ID + "= $albumId"
                    val songSortOrder = MediaStore.Audio.Media.TRACK + " ASC"
                    val songCursor = resolver.query(songUri, null, songSelection, null, songSortOrder)

                    val songCount: Int
                    var artistId: Long = -1L

                    if (songCursor != null) {
                        songCount = songCursor.count

                        if (songCount > 0) {
                            while (cursor.moveToNext()) {
                                artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                                break
                            }
                        }
                        songCursor.close()
                    }
                    albumList.add(AlbumEntity(albumId, albumName, artist, artistId, numOfSongs, albumArt.toString()))
                }
            }
            cursor.close()
        }
        return albumList
    }

    private fun getFirstAlbumArtUri(context: Context, artistId: Long) : Uri? {
        val resolver = context.contentResolver
        val uri = MediaStore.Audio.Artists.Albums.getContentUri("external", artistId)
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID
        )
        val cursor = resolver.query(uri, projection, null, null, null)
        val count: Int

        if (cursor != null) {
            count = cursor.count

            if (count > 0) {
                while (cursor.moveToNext()) {
                    val albumIdLong = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
                    cursor.close()
                    return getAlbumArtUri(albumIdLong)
                }
            }
        }
        return null
    }

    private fun formatDuration(duration: String): String {
        var mutableDuration = duration

        val time = Integer.valueOf(mutableDuration)

        var seconds = time / 1000
        val minutes = seconds / 60
        seconds %= 60

        mutableDuration = if (seconds < 10) {
            "$minutes:0$seconds"
        } else {
            "$minutes:$seconds"
        }
        return mutableDuration
    }

    private fun getAlbumArtUri(paramInt: Long): Uri {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), paramInt)
    }
}