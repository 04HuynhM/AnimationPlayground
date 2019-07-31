package com.badap.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badap.adapters.AlbumRecyclerAdapter
import com.badap.R
import com.badap.utilities.HelperMethods
import com.badap.utilities.MediaStoreHelper


class AlbumsFragment : Fragment() {

    lateinit var gridLayoutManager1: GridLayoutManager
    lateinit var gridLayoutManager2: GridLayoutManager
    private lateinit var gridLayoutManager3: GridLayoutManager
    private lateinit var gridLayoutManager4: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_albums, container, false)

        gridLayoutManager1 = GridLayoutManager(requireContext(), 1)
        gridLayoutManager2 = GridLayoutManager(requireContext(), 2)
        gridLayoutManager3 = GridLayoutManager(requireContext(), 3)
        gridLayoutManager4 = GridLayoutManager(requireContext(), 4)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = arguments
        val artistId = bundle?.get("artistId") as Long

        val mediaHelper = MediaStoreHelper()
        val albumList = mediaHelper.getAllAlbumsForArtist(requireContext(), artistId)

        val helperMethods = HelperMethods()
        val activity = requireActivity()

        val preferences = requireContext().getSharedPreferences("zoom_level", Context.MODE_PRIVATE)
        val editor = preferences?.edit()
        val currentViewMode = preferences.getInt("columnCount", 1)

        val recyclerView = view.findViewById<RecyclerView>(R.id.albums_recycler)

        val currentLayoutManager = getLayoutManager(currentViewMode)
        recyclerView.layoutManager = currentLayoutManager

        val adapter = AlbumRecyclerAdapter(albumList, activity, helperMethods.getScreenSize(activity).x)
        adapter.setSpan(currentViewMode)

        recyclerView.adapter = adapter

        val scaleGestureDetector = ScaleGestureDetector(requireContext(), object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                super.onScale(detector)

                var viewMode = preferences.getInt("columnCount", 1)

                detector?.let {
                    if (it.currentSpan > 200 && it.timeDelta > 200) {
                        if (it.currentSpan - it.previousSpan < -1) {
                            if (viewMode < 6) {
                                viewMode++
                                recyclerView.layoutManager = getLayoutManager(viewMode)
                                adapter.setSpan(viewMode)
                                editor?.putInt("columnCount", viewMode)?.apply()
                                return true
                            }
                        }
                        else if(it.currentSpan - it.previousSpan > 1) {
                            if (viewMode > 1) {
                                viewMode--
                                recyclerView.layoutManager = getLayoutManager(viewMode)
                                adapter.setSpan(viewMode)
                                editor?.putInt("columnCount", viewMode)?.apply()
                                return true
                            }
                        }
                    }
                }
                return false
            }
        })

        recyclerView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                scaleGestureDetector.onTouchEvent(p1)
                return false
            }
        })
    }

    private fun getLayoutManager(ViewMode: Int) : GridLayoutManager {
        return when(ViewMode) {
            1 -> gridLayoutManager2
            2 -> gridLayoutManager1
            3 -> gridLayoutManager1
            4 -> gridLayoutManager3
            5 -> gridLayoutManager4
            else -> gridLayoutManager1
        }
    }
}
