package com.badap.adapters.viewHolders.rows

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.badap.Album
import com.badap.R
import com.badap.utilities.HelperMethods

class LargeAlbumRow(v: View, activity: FragmentActivity) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private var view: View = v
    private var album: Album? = null
    private var nameViewTextView : TextView? = null
    private var numOfSongsTextView: TextView? = null
    private var albumArtistTextView: TextView? = null
    private var albumImage: ImageView? = null
    var mContext = activity
    private val helper = HelperMethods()

    init {
        nameViewTextView = view.findViewById(R.id.large_row_album_name)
        numOfSongsTextView = view.findViewById(R.id.large_row_album_song_number)
        albumImage = view.findViewById(R.id.large_row_album_image)
        albumArtistTextView = view.findViewById(R.id.large_row_album_artist)
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        helper.goToAlbumSongsFragment(album, mContext)
    }

    fun bindViewHolder(album: Album, width: Int) {
        this.album = album
        nameViewTextView?.text = album.albumName
        numOfSongsTextView?.text = album.numOfSongs
        albumArtistTextView?.text = album.artist
        albumImage?.let {
            it.layoutParams.width = width
            helper.insertImageFromUri(Uri.parse(album.albumArt), it, mContext, width)
        }
    }
}

class MediumAlbumRow(v: View, activity: FragmentActivity) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private var view: View = v
    private var album: Album? = null
    private var nameViewTextView : TextView? = null
    private var numOfSongsTextView: TextView? = null
    private var albumArtistTextView: TextView? = null
    var albumImage: ImageView? = null
    var mContext = activity
    private val helper = HelperMethods()

    init {
        nameViewTextView = view.findViewById(R.id.medium_row_album_name)
        numOfSongsTextView = view.findViewById(R.id.medium_row_album_song_number)
        albumImage = view.findViewById(R.id.medium_row_album_image)
        albumArtistTextView = view.findViewById(R.id.medium_row_album_artist)
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        helper.goToAlbumSongsFragment(album, mContext)
    }

    fun bindViewHolder(album: Album, width: Int) {
        this.album = album
        nameViewTextView?.text = album.albumName
        numOfSongsTextView?.text = album.numOfSongs
        albumArtistTextView?.text = album.artist
        albumImage?.let {
            it.layoutParams.width = width
            helper.insertImageFromUri(Uri.parse(album.albumArt), it, mContext, width)
        }
    }
}

class SmallAlbumRow(v: View, activity: FragmentActivity) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private var view: View = v
    private var album: Album? = null
    private var nameViewTextView : TextView? = null
    private var numOfSongsTextView: TextView? = null
    private var albumArtistTextView: TextView? = null
    var albumImage: ImageView? = null
    var mContext = activity
    private val helper = HelperMethods()

    init {
        nameViewTextView = view.findViewById(R.id.small_row_album_name)
        numOfSongsTextView = view.findViewById(R.id.small_row_album_song_number)
        albumImage = view.findViewById(R.id.small_row_album_image)
        albumArtistTextView = view.findViewById(R.id.small_row_album_artist)
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        helper.goToAlbumSongsFragment(album, mContext)
    }

    fun bindViewHolder(album: Album, width: Int) {
        this.album = album
        nameViewTextView?.text = album.albumName
        numOfSongsTextView?.text = album.numOfSongs
        albumArtistTextView?.text = album.artist
        albumImage?.let {
            it.layoutParams.width = width
            helper.insertImageFromUri(Uri.parse(album.albumArt), it, mContext, width)
        }
    }
}

