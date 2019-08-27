package com.badap.adapters.viewHolders.rows

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.badap.MainActivity.Companion.generalUtil
import com.badap.R
import com.badap.SongEntity

class LargeSongRow(private var view: View, private var mContext: FragmentActivity)
    : RecyclerView.ViewHolder(view), View.OnClickListener {
    private var song: SongEntity? = null
    private var mSongNameView : TextView? = null
    private var mArtistName: TextView? = null
    private var mAlbumName: TextView? = null
    private var mDuration: TextView? = null
    private var mAlbumArt: ImageView? = null

    init {
        mSongNameView = this.view.findViewById(R.id.custom_row_song_name)
        mArtistName = this.view.findViewById(R.id.custom_row_song_artist)
        mAlbumName = this.view.findViewById(R.id.custom_row_song_album)
        mDuration = this.view.findViewById(R.id.custom_row_song_duration)
        mAlbumArt = this.view.findViewById(R.id.custom_row_song_image)
        view.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        generalUtil.goToSongFragment(song, mContext)
    }

    fun bindViewHolder(song: SongEntity, width: Int) {
        this.song = song
        mSongNameView?.text = song.title
        mArtistName?.text = song.artist
        mAlbumName?.text = song.album
        mDuration?.text = song.duration
        mAlbumArt?.let {
            it.layoutParams.width = width
            generalUtil.insertImageFromUri(Uri.parse(song.albumArtUriString), it, mContext, width)
        }
    }
}

class MediumSongRow(v: View, private val mContext: FragmentActivity)
    : RecyclerView.ViewHolder(v), View.OnClickListener {

    private var view: View = v
    private var song: SongEntity? = null
    private var mSongNameView : TextView? = null
    private var mArtistName: TextView? = null
    private var mDuration: TextView? = null
    private var mAlbumArt: ImageView? = null

    init {
        mSongNameView = view.findViewById(R.id.medium_row_song_name)
        mArtistName = view.findViewById(R.id.medium_row_song_artist_album)
        mDuration = view.findViewById(R.id.medium_row_song_duration)
        mAlbumArt = view.findViewById(R.id.medium_row_song_image)
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        generalUtil.goToSongFragment(song, mContext)
    }

    fun bindViewHolder(song: SongEntity, width: Int) {
        this.song = song
        mSongNameView?.text = song.title
        mArtistName?.text = "${song.artist} | ${song.album}"
        mDuration?.text = song.duration
        mAlbumArt?.let {
            it.layoutParams.width = width
            generalUtil.insertImageFromUri(Uri.parse(song.albumArtUriString), it, mContext, width)
        }
    }
}

class SmallSongRow(view: View,
                   private var mContext: FragmentActivity)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

    private var song: SongEntity? = null
    private var mSongNameView : TextView? = null
    private var mArtistAlbumText: TextView? = null
    private var mDuration: TextView? = null
    private var mAlbumArt: ImageView? = null

    init {
        mSongNameView = view.findViewById(R.id.small_row_song_name)
        mArtistAlbumText = view.findViewById(R.id.small_row_song_artist_album)
        mDuration = view.findViewById(R.id.small_row_song_duration)
        mAlbumArt = view.findViewById(R.id.small_row_song_image)
        view.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        generalUtil.goToSongFragment(song, mContext)
    }

    fun bindViewHolder(song: SongEntity, width: Int) {
        this.song = song
        mSongNameView?.text = song.title
        mArtistAlbumText?.text = "${song.artist} | ${song.album}"
        mDuration?.text = song.duration
        mAlbumArt?.let {
            it.layoutParams.width = width
            generalUtil.insertImageFromUri(Uri.parse(song.albumArtUriString), it, mContext, width)
        }
    }
}