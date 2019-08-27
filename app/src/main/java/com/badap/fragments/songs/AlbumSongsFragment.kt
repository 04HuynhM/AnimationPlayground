package com.badap.fragments.songs

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.edit
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badap.MainActivity
import com.badap.MainActivity.Companion.mediaStoreUtil
import com.badap.R
import com.badap.adapters.SongRecyclerAdapter
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
        return inflater.inflate(R.layout.fragment_single_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = arguments
        val albumName = bundle?.get("albumName").toString()
        val albumId = bundle?.get("albumId").toString()
        val albumArtUriString = bundle?.get("albumArt").toString()
        val albumArtUri = Uri.parse(albumArtUriString)
        val albumArtImageView = view.findViewById<ImageView>(R.id.single_album_art)

        Glide.with(requireContext())
            .load(albumArtUri)
            .placeholder(R.drawable.ic_musical_note_and_stave)
            .centerCrop()
            .transform(RoundedCorners(25))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(albumArtImageView)

        val recyclerView = view.findViewById<RecyclerView>(R.id.album_songs_recycler)
        val albumNameTextView = view.findViewById<TextView>(R.id.album_songs_title)

        val spannableArtist = MainActivity.generalUtil.setSpannableString(albumName, requireActivity().getColor(R.color.timecode_shadow))
        albumNameTextView?.text = spannableArtist

        val playButton = view.findViewById<Button>(R.id.album_songs_play_button)
        val shuffleButton = view.findViewById<Button>(R.id.album_songs_shuffle_button)
        val backButton = view.findViewById<Button>(R.id.album_songs_back_button)
        val optionsButton = view.findViewById<Button>(R.id.album_songs_options_button)
        val navView = view.findViewById<DrawerLayout>(R.id.single_album_container)

        playButton.setOnClickListener {
            Toast.makeText(requireContext(), "Play all", Toast.LENGTH_SHORT).show()
        }

        shuffleButton.setOnClickListener {
            Toast.makeText(requireContext(), "Shuffle all", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            Toast.makeText(requireContext(), "Back all", Toast.LENGTH_SHORT).show()
        }

        optionsButton.setOnClickListener {
            Toast.makeText(requireActivity(), "Options yay!", Toast.LENGTH_SHORT).show()
            navView.openDrawer(Gravity.RIGHT)
        }

        val songList = mediaStoreUtil.getAllSongsForAlbum(requireContext(), albumId)

        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val initialViewType = prefs.getInt("view_mode", 1)
        val screenWidth = prefs.getInt("screen_width", -1)

        val layoutManager = GridLayoutManager(requireContext(), 12)
        recyclerView.layoutManager = layoutManager

        adapter = SongRecyclerAdapter(songList, requireActivity(), screenWidth, initialViewType)
        recyclerView.adapter = adapter

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

        val viewTypeRadioGroup: RadioGroup = view.findViewById(R.id.songs_drawer_viewtype_radiogroup)

        viewTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.songs_drawer_row_large -> {
                    setViewType(2)
                }
                R.id.songs_drawer_row_medium -> {
                    setViewType(3)
                }
                R.id.songs_drawer_row_small -> {
                    setViewType(6)
                }
                R.id.songs_drawer_grid_large -> {
                    setViewType(1)
                }
                R.id.songs_drawer_grid_medium -> {
                    setViewType(4)
                }
                else -> {
                    setViewType(5)
                }
            }
        }
    }

    fun setViewType(viewType: Int) {
        adapter.setViewType(viewType)
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
        prefs.edit { putInt("view_mode", viewType)?.apply() }
    }
}
