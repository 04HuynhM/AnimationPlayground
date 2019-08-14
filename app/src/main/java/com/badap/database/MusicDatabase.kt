package com.badap.database

import android.annotation.SuppressLint
import android.content.AsyncQueryHandler
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.badap.AlbumEntity
import com.badap.ArtistEntity
import com.badap.SongEntity


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

        @Volatile private var INSTANCE: MusicDatabase? = null

        fun getInstance(context: Context): MusicDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance
                        = Room.databaseBuilder(context.applicationContext,
                    MusicDatabase::class.java,
                    "indexed_library")
                    .addCallback(getCallback(context))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private fun getCallback(context: Context) : Callback {
            return object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    INSTANCE?.let { db ->
                        val mso = MediaStoreOperations()
                        mso.populateSongsTable(context, db)
                        mso.populateAlbumsTable(context, db)
                        mso.populateArtistsTable(context, db)
                    }
                }
            }
        }
    }

    class MediaStoreOperations {

        fun populateSongsTable(context: Context, database: MusicDatabase?) {
            val songDao = database?.songDao()

            val resolver = context.contentResolver

            val asyncQueryHandler = @SuppressLint("HandlerLeak")
            object : AsyncQueryHandler(resolver) {
                override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
                    super.onQueryComplete(token, cookie, cursor)
                    if (cursor != null) {
                        if (cursor.count > 0) {
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
                                songDao?.addSong(
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
                }
            }

            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
            val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

            asyncQueryHandler.startQuery(3, null, uri, null, selection, null, sortOrder)
        }

        fun populateArtistsTable(context: Context, database: MusicDatabase?) {
            val artistDao = database?.artistDao()

            val resolver = context.contentResolver

            val asyncQueryHandler = @SuppressLint("HandlerLeak")
            object : AsyncQueryHandler(resolver) {
                override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
                    super.onQueryComplete(token, cookie, cursor)
                    if (cursor != null) {
                        if (cursor.count > 0) {
                            while (cursor.moveToNext()) {
                                val artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID))
                                val artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))
                                val numOfTracks =
                                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))
                                val numOfAlbums =
                                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))
                                val artistIdLong = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID))
                                val albumArtUri = getFirstAlbumArtUri(context, artistIdLong)

                                artistDao?.addArtist(
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
                    Log.i("get_artists_complete", "Artist data added to room db")
                }
            }
            val uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
            )
            val sortOrder = MediaStore.Audio.Artists.ARTIST + " ASC"
            asyncQueryHandler.startQuery(2, null, uri, projection, null, null, sortOrder)
        }

        fun populateAlbumsTable(context: Context, database: MusicDatabase?) {
            val albumDao = database?.albumDao()

            val resolver = context.contentResolver

            val asyncQueryHandler = @SuppressLint("HandlerLeak")
            object : AsyncQueryHandler(resolver) {
                override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
                    super.onQueryComplete(token, cookie, cursor)

                    if (cursor != null) {
                        if (cursor.count > 0) {
                            while (cursor.moveToNext()) {
                                val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
                                val albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
                                val numOfSongs =
                                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
                                val albumArt = getAlbumArtUri(albumId)

                                val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                val songSelection = MediaStore.Audio.Media.ALBUM_ID + "= $albumId"
                                val songSortOrder = MediaStore.Audio.Media.TRACK + " ASC"
                                val songCursor = resolver.query(songUri, null, songSelection, null, songSortOrder)

                                var artistId: Long = -1L
                                if (songCursor != null) {
                                    if (songCursor.count > 0) {
                                        cursor.moveToFirst()
                                        artistId =
                                            cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                                    }
                                    songCursor.close()
                                }
                                albumDao?.addAlbum(
                                    AlbumEntity(
                                        albumId,
                                        albumName,
                                        artist,
                                        artistId,
                                        numOfSongs,
                                        albumArt.toString()
                                    )
                                )
                            }
                        }
                        cursor.close()
                    }
                    Log.i("get_albums_complete", "Album data added to room db")
                }
            }
            val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.ALBUM_ART
            )
            val sortOrder = MediaStore.Audio.Albums.ALBUM + " ASC"

            asyncQueryHandler.startQuery(1, null, uri, projection, null, null, sortOrder)
        }

        private fun getFirstAlbumArtUri(context: Context, artistId: Long): Uri? {
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
                    cursor.moveToFirst()
                    val albumIdLong = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
                    cursor.close()
                    return getAlbumArtUri(albumIdLong)
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
}