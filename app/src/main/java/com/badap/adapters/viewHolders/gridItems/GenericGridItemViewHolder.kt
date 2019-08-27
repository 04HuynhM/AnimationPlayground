package com.badap.adapters.viewHolders.gridItems

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.badap.AlbumEntity
import com.badap.MainActivity.Companion.generalUtil
import com.badap.R
import com.badap.SongEntity

class GenericGridItemViewHolder(view: View,
                                private var activity: FragmentActivity,
                                private var width: Int)
    : RecyclerView.ViewHolder(view), View.OnClickListener {
    private var data: Any? = null
    private var mTitleView : TextView? = null
    private var mDetailView: TextView? = null
    private var mImageView: ImageView? = null
    private var mContainer: ConstraintLayout? = null

    init {
        mTitleView = view.findViewById(R.id.generic_grid_item_name)
        mDetailView = view.findViewById(R.id.generic_grid_item_detail)
        mImageView = view.findViewById(R.id.generic_grid_item_image)
        mContainer = view.findViewById(R.id.artist_grid_container)
        view.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(val data = this.data) {
            is AlbumEntity -> {
                generalUtil.goToAlbumSongsFragment(data, activity)
            }
            is SongEntity -> {
                generalUtil.goToSongFragment(data, activity)
            }
        }
    }

    fun bindViewHolder(data: Any) {
        val colorId = activity.getColor(R.color.timecode_shadow)
        this.data = data
        when(data) {
            is AlbumEntity -> {
                val spannableName = generalUtil.setSpannableString(data.name, colorId)
                val spannableArtist = generalUtil.setSpannableString(data.artist, colorId)
                mDetailView?.text = spannableArtist
                mTitleView?.text = spannableName
                mImageView?.let {
                    generalUtil.insertImageFromUri(Uri.parse(data.albumArtUriString), mImageView!!, activity, width)
                }
            }
            is SongEntity -> {
                val spannableName = generalUtil.setSpannableString(data.title, colorId)
                val spannableAlbum = generalUtil.setSpannableString(data.album, colorId)
                mDetailView?.text = spannableAlbum
                mTitleView?.text = spannableName
                mImageView?.let {
                    generalUtil.insertImageFromUri(Uri.parse(data.albumArtUriString), mImageView!!, activity, width)
                }
            }
        }
    }
}
