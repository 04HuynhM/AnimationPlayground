package com.badap.utilities

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
import com.badap.fragments.AlbumsFragment
import com.badap.fragments.SingleAlbumFragment
import com.badap.fragments.SongFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class HelperMethods {
    fun goToSingleAlbumFragment(album: Album?, context: FragmentActivity?) {
        val singleAlbumFragment = SingleAlbumFragment()
        val bundle = Bundle()
        bundle.putString("albumArt", album?.albumArt.toString())
        bundle.putString("albumId", album?.albumId)
        singleAlbumFragment.arguments = bundle

        context?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_container, singleAlbumFragment)
            ?.addToBackStack("single_album_songs_fragment")
            ?.commit()
    }

    fun goToAlbumListFragment(artist: Artist?, context: FragmentActivity?) {
        val albumsFragment = AlbumsFragment()
        val bundle = Bundle()
        artist?.artistIdLong?.let { bundle.putLong("artistId", it) }
        albumsFragment.arguments = bundle
        context?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_container, albumsFragment)
            ?.addToBackStack("albums_for_artist_fragment")
            ?.commit()
    }

    fun goToSongFragment(song: Song?, context: FragmentActivity?) {
        val songFragment = SongFragment()
        val bundle = Bundle()
        bundle.putString("songPath", song?.path)
        bundle.putString("duration", song?.rawDuration)
        songFragment.arguments = bundle
        context?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_container, songFragment)
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

    fun getScreenSize(activity: FragmentActivity) : Point {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }
}