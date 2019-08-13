package com.badap.adapters.adapterViewHolders

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.badap.ArtistEntity
import com.badap.MainActivity
import com.badap.MainActivity.Companion.generalUtil
import com.badap.R

class LargeArtistRow(v: View, activity: FragmentActivity) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private var view: View = v
    private var artist: ArtistEntity? = null
    private var mNameView : TextView? = null
    private var mNumOfSongs: TextView? = null
    private var mNumOfAlbums: TextView? = null
    private var mArtistImage: ImageView? = null
    private var mContext = activity

    init {
        mNameView = view.findViewById(R.id.custom_row_artist_name)
        mNumOfSongs = view.findViewById(R.id.custom_row_artist_song_number)
        mNumOfAlbums = view.findViewById(R.id.custom_row_artist_album_number)
        mArtistImage = view.findViewById(R.id.custom_row_artist_image)
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        generalUtil.goToAlbumListFragment(artist, mContext)
    }

    fun bindViewHolder(artist: ArtistEntity, width: Int) {
        this.artist = artist
        mNameView?.text = artist.name
        mNumOfSongs?.text = artist.numberOfTracks
        mNumOfAlbums?.text = artist.numberOfAlbums
        mArtistImage?.let {
            it.layoutParams.width = width
            generalUtil.insertImageFromUri(Uri.parse(artist.firstAlbumArt), it, mContext, width)
        }
    }
}

class MediumArtistRow(v: View, activity: FragmentActivity) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private var view: View = v
    private var artist: ArtistEntity? = null
    private var mNameView : TextView? = null
    private var mNumOfSongs: TextView? = null
    private var mNumOfAlbums: TextView? = null
    private var mArtistImage: ImageView? = null
    private var mContext = activity

    init {
        mNameView = view.findViewById(R.id.medium_row_artist_name)
        mNumOfSongs = view.findViewById(R.id.medium_row_artist_song_number)
        mNumOfAlbums = view.findViewById(R.id.medium_row_artist_album_number)
        mArtistImage = view.findViewById(R.id.medium_row_artist_image)
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        generalUtil.goToAlbumListFragment(artist, mContext)
    }

    fun bindViewHolder(artist: ArtistEntity, width: Int) {
        this.artist = artist
        mNameView?.text = artist.name
        mNumOfSongs?.text = artist.numberOfTracks
        mNumOfAlbums?.text = artist.numberOfAlbums
        mArtistImage?.let {
            it.layoutParams.width = width
            generalUtil.insertImageFromUri(Uri.parse(artist.firstAlbumArt), it, mContext, width)
        }
    }
}

class SmallArtistRow(v: View, activity: FragmentActivity) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private var view: View = v
    private var artist: ArtistEntity? = null
    private var mNameView : TextView? = null
    private var mNumOfSongs: TextView? = null
    private var mNumOfAlbums: TextView? = null
    private var mArtistImage: ImageView? = null
    private var mContext = activity

    init {
        mNameView = view.findViewById(R.id.small_row_artist_name)
        mNumOfSongs = view.findViewById(R.id.small_row_artist_song_number)
        mNumOfAlbums = view.findViewById(R.id.small_row_artist_album_number)
        mArtistImage = view.findViewById(R.id.small_row_artist_image)
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        generalUtil.goToAlbumListFragment(artist, mContext)
    }

    fun bindViewHolder(artist: ArtistEntity, width: Int) {
        this.artist = artist
        mNameView?.text = artist.name
        mNumOfSongs?.text = artist.numberOfTracks
        mNumOfAlbums?.text = artist.numberOfAlbums
        mArtistImage?.let {
            it.layoutParams.width = width
            generalUtil.insertImageFromUri(Uri.parse(artist.firstAlbumArt), it, mContext, width)
        }
    }
}

//Multiple Span Grid Layout ViewHolders
class ArtistGridItem(v: View, activity: FragmentActivity, private val width: Int)
    : RecyclerView.ViewHolder(v), View.OnClickListener {
    private var view: View = v
    private var artist: ArtistEntity? = null
    private var mNameView : TextView? = null
    private var mArtistImage: ImageView? = null
    private var mContext = activity

    init {
        mNameView = view.findViewById(R.id.artist_grid_item_name)
        mArtistImage = view.findViewById(R.id.artist_grid_image)
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        generalUtil.goToAlbumListFragment(artist, mContext)
    }

    fun bindViewHolder(artist: ArtistEntity) {
        this.artist = artist
        val colorId = mContext.getColor(R.color.timecode_shadow)

        val artistName = MainActivity.generalUtil.setSpannableString(artist.name, colorId)

        mNameView?.text = artistName
        mArtistImage?.let {
            it.layoutParams.width = width
            generalUtil.insertImageFromUri(Uri.parse(artist.firstAlbumArt), it, mContext, width)
        }
    }
}