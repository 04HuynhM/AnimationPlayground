package com.badap.fragments.songs

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badap.R
import com.badap.adapters.SongRecyclerAdapter
import com.badap.utilities.MediaStoreHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AlbumSongsFragment : Fragment() {

    lateinit var prefs: SharedPreferences
    lateinit var adapter: SongRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_single_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = arguments
        requireActivity().title = bundle?.get("albumName").toString()
        val albumId = bundle?.get("albumId").toString()
        val albumArtUriString = bundle?.get("albumArt").toString()
        val albumArtUri = Uri.parse(albumArtUriString)

        val albumArtImageView = view.findViewById<ImageView>(R.id.single_album_art)
        val recyclerView = view.findViewById<RecyclerView>(R.id.single_album_song_recycler)

        val mediaHelper = MediaStoreHelper()
        val songList = mediaHelper.getAllSongsForAlbum(requireContext(), albumId)

        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val initialViewType = prefs.getInt("view_mode", 1)
        val screenWidth = prefs.getInt("screen_width", -1)

        val layoutManager = GridLayoutManager(requireContext(), 12)
        recyclerView.layoutManager = layoutManager

        adapter = SongRecyclerAdapter(songList, requireActivity(), albumArtUri, screenWidth, initialViewType)
        recyclerView.adapter = adapter

        Glide.with(requireContext())
            .load(albumArtUri)
            .placeholder(R.drawable.ic_musical_note_and_stave)
            .centerCrop()
            .transform(RoundedCorners(25))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(albumArtImageView)

        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(adapter.getItemViewType(position)) {
                    0 -> 6 // large grid
                    1 -> 12 // large row
                    2 -> 12 // medium row
                    3 -> 4 // medium grid
                    4 -> 3 // small grid
                    5 -> 12 // small row
                    else -> -1
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.large_grid_option -> {
                setViewType(1)
            }
            R.id.large_row_option -> {
                setViewType(2)
            }
            R.id.medium_grid_option -> {
                setViewType(4)
            }
            R.id.medium_row_option -> {
                setViewType(3)
            }
            R.id.small_grid_option -> {
                setViewType(5)
            }
            R.id.small_row_option -> {
                setViewType(6)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setViewType(viewType: Int) {
        adapter.setViewType(viewType)
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
        prefs.edit { putInt("view_mode", viewType)?.apply() }
    }
}
