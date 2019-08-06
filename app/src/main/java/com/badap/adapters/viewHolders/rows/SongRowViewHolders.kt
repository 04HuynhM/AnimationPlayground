package com.badap.adapters.viewHolders.rows

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.badap.R
import com.badap.Song
import com.badap.utilities.HelperMethods

class LargeSongRow(private var view: View, private var mContext: FragmentActivity, albumArtUri: Uri)
    : RecyclerView.ViewHolder(view), View.OnClickListener {
    private var song: Song? = null
    private var mSongNameView : TextView? = null
    private var mArtistName: TextView? = null
    private var mAlbumName: TextView? = null
    private var mDuration: TextView? = null
    private var mAlbumArt: ImageView? = null
    private val helper = HelperMethods()
    private val mAlbumArtUri: Uri = albumArtUri

    init {
        mSongNameView = this.view.findViewById(R.id.custom_row_song_name)
        mArtistName = this.view.findViewById(R.id.custom_row_song_artist)
        mAlbumName = this.view.findViewById(R.id.custom_row_song_album)
        mDuration = this.view.findViewById(R.id.custom_row_song_duration)
        mAlbumArt = this.view.findViewById(R.id.custom_row_song_image)
        view.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        helper.goToSongFragment(song, mContext)
    }

    fun bindViewHolder(song: Song, width: Int) {
        this.song = song
        mSongNameView?.text = song.name
        mArtistName?.text = song.artist
        mAlbumName?.text = song.album
        mDuration?.text = song.duration
        mAlbumArt?.let {
            helper.insertImageFromUri(mAlbumArtUri, it, mContext, width)
        }
    }
}

class MediumSongRow(v: View, private val mContext: FragmentActivity, albumArtUri: Uri)
    : RecyclerView.ViewHolder(v), View.OnClickListener {

    private var view: View = v
    private var song: Song? = null
    private var mSongNameView : TextView? = null
    private var mArtistName: TextView? = null
    private var mAlbumName: TextView? = null
    private var mDuration: TextView? = null
    private var mAlbumArt: ImageView? = null
    private val helper = HelperMethods()
    private val mAlbumArtUri: Uri = albumArtUri

    init {
        mSongNameView = view.findViewById(R.id.custom_row_song_name)
        mArtistName = view.findViewById(R.id.custom_row_song_artist)
        mAlbumName = view.findViewById(R.id.custom_row_song_album)
        mDuration = view.findViewById(R.id.custom_row_song_duration)
        mAlbumArt = view.findViewById(R.id.custom_row_song_image)
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        helper.goToSongFragment(song, mContext)
    }

    fun bindViewHolder(song: Song, width: Int) {
        this.song = song
        mSongNameView?.text = song.name
        mArtistName?.text = song.artist
        mAlbumName?.text = song.album
        mDuration?.text = song.duration
        mAlbumArt?.let {
            helper.insertImageFromUri(mAlbumArtUri, it, mContext, width)
        }
    }
}

class SmallSongRow(view: View,
                   private var mContext: FragmentActivity,
                   albumArtUri: Uri)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

    private var song: Song? = null
    private var mSongNameView : TextView? = null
    private var mArtistName: TextView? = null
    private var mAlbumName: TextView? = null
    private var mDuration: TextView? = null
    private var mAlbumArt: ImageView? = null
    private val helper = HelperMethods()
    private val mAlbumArtUri: Uri = albumArtUri

    init {
        mSongNameView = view.findViewById(R.id.custom_row_song_name)
        mArtistName = view.findViewById(R.id.custom_row_song_artist)
        mAlbumName = view.findViewById(R.id.custom_row_song_album)
        mDuration = view.findViewById(R.id.custom_row_song_duration)
        mAlbumArt = view.findViewById(R.id.custom_row_song_image)
        view.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        helper.goToSongFragment(song, mContext)
    }

    fun bindViewHolder(song: Song, width: Int) {
        this.song = song
        mSongNameView?.text = song.name
        mArtistName?.text = song.artist
        mAlbumName?.text = song.album
        mDuration?.text = song.duration
        mAlbumArt?.let {
            helper.insertImageFromUri(mAlbumArtUri, it, mContext, width)
        }
    }
}