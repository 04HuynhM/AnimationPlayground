package com.badap.utilities

import android.app.Activity
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.badap.*
import com.badap.fragments.albums.AlbumsFragment
import com.badap.fragments.songs.AlbumSongsFragment
import com.badap.fragments.songs.PlayerFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class GeneralUtility {
    fun goToAlbumSongsFragment(album: AlbumEntity?, context: FragmentActivity?) {
        val singleAlbumFragment = AlbumSongsFragment()
        val bundle = Bundle()
        album?.let {
            bundle.putString("albumArtUriString", it.albumArtUriString.toString())
            bundle.putLong("albumId", it.albumId)
            bundle.putString("name", it.name)
        }
        singleAlbumFragment.arguments = bundle
        context?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_container, singleAlbumFragment)
            ?.addToBackStack("single_album_songs_fragment")
            ?.commit()
    }

    fun goToAlbumListFragment(artist: ArtistEntity?, context: FragmentActivity?) {
        val albumsFragment = AlbumsFragment()
        val bundle = Bundle()
        artist?.let {
            bundle.putLong("artistId", it.artistIdLong)
            bundle.putString("name", it.name)
        }
        albumsFragment.arguments = bundle
        context?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_container, albumsFragment)
            ?.addToBackStack("albums_for_artist_fragment")
            ?.commit()
    }

    fun goToSongFragment(song: SongEntity?, context: FragmentActivity?) {
        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putString("songJson", song?.toJson())
        playerFragment.arguments = bundle
        context?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_container, playerFragment)
            ?.addToBackStack("song_fragment")
            ?.commit()
    }

    fun insertImageFromUri(uri: Uri?, imageView: ImageView, activity: FragmentActivity, width: Int?) {
        if (width != null) {
            Glide.with(activity)
                .load(uri)
                .placeholder(R.drawable.ic_musical_note_and_stave)
                .centerCrop()
                .transform(RoundedCorners(16))
                .override(width, width)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        } else {
            Glide.with(activity)
                .load(uri)
                .placeholder(R.drawable.ic_musical_note_and_stave)
                .centerCrop()
                .transform(RoundedCorners(16))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }

    fun getInflatedView(parent: ViewGroup, layoutId: Int) : View {
        return parent.inflate(layoutId, false)
    }

    fun setSpannableString(string: String, colorId: Int): SpannableString {
        val spannable = SpannableString(string)
        spannable.setSpan(BackgroundColorSpan(colorId), 0, spannable.length, 0)
        return spannable
    }

    fun getScreenSize(activity: Activity) : Point {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }
}