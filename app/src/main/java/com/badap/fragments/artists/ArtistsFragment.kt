package com.badap.fragments.artists


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badap.MainActivity
import com.badap.R
import com.badap.adapters.ArtistRecyclerAdapter

class ArtistsFragment : Fragment() {

    lateinit var adapter: ArtistRecyclerAdapter
    lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.artists_recycler)
        val playButton = view.findViewById<Button>(R.id.artists_play_button)
        val shuffleButton = view.findViewById<Button>(R.id.artists_shuffle_button)
        val backButton = view.findViewById<Button>(R.id.artists_back_button)
        val optionsButton = view.findViewById<Button>(R.id.artists_options_button)
        val navView = view.findViewById<DrawerLayout>(R.id.artists_container)

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

        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val initialViewMode = prefs.getInt("view_mode", 2)
        val screenWidth = prefs.getInt("screen_width", -1)

        val layoutManager = GridLayoutManager(requireContext(), 12)
        recyclerView.layoutManager = layoutManager
        MainActivity.indexedArtists?.let {
            adapter = ArtistRecyclerAdapter(it, requireActivity(), screenWidth, initialViewMode)
            recyclerView.adapter = adapter
        }
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
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

        val viewTypeRadioGroup: RadioGroup = view.findViewById(R.id.artists_drawer_viewtype_radiogroup)

        viewTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.artists_drawer_row_large -> {
                    setViewType(2)
                }
                R.id.artists_drawer_row_medium -> {
                    setViewType(3)
                }
                R.id.artists_drawer_row_small -> {
                    setViewType(6)
                }
                R.id.artists_drawer_grid_large -> {
                    setViewType(1)
                }
                R.id.artists_drawer_grid_medium -> {
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
