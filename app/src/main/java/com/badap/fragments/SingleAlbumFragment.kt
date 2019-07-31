package com.badap.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badap.adapters.SongRecyclerAdapter
import com.badap.R
import com.badap.utilities.MediaStoreHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class SingleAlbumFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle? = arguments
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

        if (!albumId.isBlank()) {
            val helper = MediaStoreHelper()

            val oneRowGridLayoutManager = GridLayoutManager(requireContext(), 1)
            val recyclerView = view.findViewById<RecyclerView>(R.id.single_album_song_recycler)

            recyclerView.layoutManager = oneRowGridLayoutManager
            val adapter = SongRecyclerAdapter(helper.getAllSongsForAlbum(requireContext(), albumId),
                requireActivity(),
                albumArtUri,
                oneRowGridLayoutManager)

            recyclerView.adapter = adapter
        } else {
            Toast.makeText(context, "No Album ID", Toast.LENGTH_LONG).show()
        }
    }
}
