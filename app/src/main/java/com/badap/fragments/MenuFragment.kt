package com.badap.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.badap.MainActivity
import com.badap.R
import com.badap.fragments.albums.AllAlbumsFragment
import com.badap.fragments.artists.ArtistsFragment
import com.badap.fragments.songs.AllSongsFragment

class MenuFragment : Fragment() {

    lateinit var container: MotionLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        requireActivity().title = "Library"
        return inflater.inflate(R.layout.menu_layout_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu_options, menu)
        val mainViewTypeOption = menu.findItem(R.id.button_viewmode_option)
        mainViewTypeOption.isVisible = false
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update_library_index_main -> {
                val activity = requireActivity() as MainActivity
                activity.initializeLibraryArrays()
            }
            R.id.button_viewmode_option_main -> {
                if (container.currentState == container.startState) {
                    container.transitionToEnd()
                    item.icon = resources.getDrawable(R.drawable.ic_view_list_white_24dp, null)
                } else {
                    container.transitionToStart()
                    item.icon = resources.getDrawable(R.drawable.ic_view_module_white_24dp, null)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val artistsButton = view.findViewById<Button>(R.id.button_artists_main)
        val albumsButton = view.findViewById<Button>(R.id.button_albums_main)
        val allSongsButton = view.findViewById<Button>(R.id.button_all_songs_main)
        val settingsButton = view.findViewById<Button>(R.id.button_settings_main)
        container = view.findViewById(R.id.motionlayout_container_main)

        artistsButton.setOnClickListener {
            loadFragment(ArtistsFragment())
        }

        albumsButton.setOnClickListener {
            loadFragment(AllAlbumsFragment())
        }

        allSongsButton.setOnClickListener {
            loadFragment(AllSongsFragment())
        }

        settingsButton.setOnClickListener {

        }

    }

    private fun loadFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack("from_main_menu")
            .commit()
    }
}
