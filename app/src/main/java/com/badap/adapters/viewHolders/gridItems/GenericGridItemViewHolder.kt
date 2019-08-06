package com.badap.adapters.viewHolders.gridItems

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.badap.Album
import com.badap.R
import com.badap.Song
import com.badap.utilities.HelperMethods

class GenericGridItemViewHolder(view: View,
                                private var activity: FragmentActivity,
                                private var width: Int,
                                private var image: Uri?)
    : RecyclerView.ViewHolder(view), View.OnClickListener {
    private var data: Any? = null
    private var mTitleView : TextView? = null
    private var mDetailView: TextView? = null
    private var mImageView: ImageView? = null
    private var mContainer: ConstraintLayout? = null

    val helper = HelperMethods()

    init {
        mTitleView = view.findViewById(R.id.generic_grid_item_name)
        mDetailView = view.findViewById(R.id.generic_grid_item_detail)
        mImageView = view.findViewById(R.id.generic_grid_item_image)
        mContainer = view.findViewById(R.id.artist_grid_container)
        view.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(val data = this.data) {
            is Album -> {
                helper.goToAlbumSongsFragment(data, activity)
            }
            is Song -> {
                helper.goToSongFragment(data, activity)
            }
        }
    }

    fun bindViewHolder(data: Any) {
        val colorId = activity.getColor(R.color.timecode_shadow)
        this.data = data
        when(data) {
            is Album -> {
                val spannableName = helper.setSpannableString(data.albumName, colorId)
                val spannableArtist = helper.setSpannableString(data.artist, colorId)
                mDetailView?.text = spannableArtist
                mTitleView?.text = spannableName
                mImageView?.let {
                    helper.insertImageFromUri(Uri.parse(data.albumArt), mImageView!!, activity, width)
                }
            }
            is Song -> {
                val spannableName = helper.setSpannableString(data.title, colorId)
                val spannableAlbum = helper.setSpannableString(data.album, colorId)
                mDetailView?.text = spannableAlbum
                mTitleView?.text = spannableName
                mImageView?.let {
                    helper.insertImageFromUri(image, mImageView!!, activity, width)
                }
            }
        }
    }
}
