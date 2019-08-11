package com.badap.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

import com.badap.R
import com.badap.fragments.albums.AlbumsFragment
import com.badap.fragments.albums.AllAlbumsFragment
import com.badap.fragments.artists.ArtistsFragment
import com.badap.fragments.songs.AlbumSongsFragment
import com.badap.fragments.songs.AllSongsFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BottomSheetViewModeDialog(val fragment: Fragment, val viewFab: FloatingActionButton) : BottomSheetDialogFragment() {

    private lateinit var gridRadioGroup: RadioGroup
    private lateinit var rowRadioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.setCanceledOnTouchOutside(true)

        val view = inflater.inflate(R.layout.bottom_sheet_viewmode_dialog, container, false)

        rowRadioGroup = view.findViewById(R.id.row_radio_group)
        gridRadioGroup = view.findViewById(R.id.grid_radio_group)

        val largeGridRadio = view.findViewById<RadioButton>(R.id.large_grid_radio)
        val mediumGridRadio = view.findViewById<RadioButton>(R.id.medium_grid_radio)
        val smallGridRadio = view.findViewById<RadioButton>(R.id.small_grid_radio)
        val largeRowRadio = view.findViewById<RadioButton>(R.id.large_row_radio)
        val mediumRowRadio = view.findViewById<RadioButton>(R.id.medium_row_radio)
        val smallRowRadio = view.findViewById<RadioButton>(R.id.small_row_radio)

        largeGridRadio.setOnClickListener {
            clearAllAndCheck(largeGridRadio)
            viewFab.setImageDrawable(resources.getDrawable(R.drawable.ic_view_module_black_24dp, null))
            setViewType(fragment, 1)
        }

        mediumGridRadio.setOnClickListener {
            clearAllAndCheck(mediumGridRadio)
            viewFab.setImageDrawable(resources.getDrawable(R.drawable.ic_view_module_black_24dp, null))
            setViewType(fragment, 4)
        }

        smallGridRadio.setOnClickListener {
            clearAllAndCheck(smallGridRadio)
            viewFab.setImageDrawable(resources.getDrawable(R.drawable.ic_view_module_black_24dp, null))
            setViewType(fragment, 5)
        }

        largeRowRadio.setOnClickListener {
            clearAllAndCheck(largeRowRadio)
            viewFab.setImageDrawable(resources.getDrawable(R.drawable.ic_view_list_black_24dp, null))
            setViewType(fragment, 2)
        }

        mediumRowRadio.setOnClickListener {
            clearAllAndCheck(mediumRowRadio)
            viewFab.setImageDrawable(resources.getDrawable(R.drawable.ic_view_list_black_24dp, null))
            setViewType(fragment, 3)
        }

        smallRowRadio.setOnClickListener {
            clearAllAndCheck(smallRowRadio)
            viewFab.setImageDrawable(resources.getDrawable(R.drawable.ic_view_list_black_24dp, null))
            setViewType(fragment, 6)
        }
        return view
    }

    private fun setViewType(fragment: Any, viewType: Int) {
        Log.i("Wtf", "Is this working?")
        when (fragment) {
            is AlbumsFragment -> {
                Log.i("Bottom Sheet viewtype", "Is AlbumsFragment")
                fragment.setViewType(viewType)
            }
            is AllAlbumsFragment -> {
                Log.i("Bottom Sheet viewtype", "Is AllAlbumsFragment")
                fragment.setViewType(viewType)
            }
            is ArtistsFragment -> {
                Log.i("Bottom Sheet viewtype", "Is ArtistsFragment")
                fragment.setViewType(viewType)
            }
            is AlbumSongsFragment -> {
                Log.i("Bottom Sheet viewtype", "Is AlbumSongsFragment")
                fragment.setViewType(viewType)
            }
            is AllSongsFragment -> {
                Log.i("Bottom Sheet viewtype", "Is AllSongsFragment")
                fragment.setViewType(viewType)
            }
        }
    }

    private fun clearAllAndCheck(buttonToCheck: RadioButton) {
        rowRadioGroup.clearCheck()
        gridRadioGroup.clearCheck()
        buttonToCheck.isChecked = true
    }
}
