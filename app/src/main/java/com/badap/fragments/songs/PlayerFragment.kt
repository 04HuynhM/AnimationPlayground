package com.badap.fragments.songs


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.badap.MainActivity

import com.badap.R
import com.badap.SongEntity
import com.google.gson.Gson


class PlayerFragment : Fragment() {

    private lateinit var albumArt: ImageView
    private lateinit var playButton: ImageButton
    private lateinit var shuffleButton: ImageButton
    private lateinit var fastForwardButton: ImageButton
    private lateinit var skipForwardButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var rewindButton: ImageButton
    private lateinit var sleepTimerButton: ImageButton
    private lateinit var repeatButton: ImageButton
    private lateinit var songSeekBar: SeekBar
    private lateinit var songNameTextView: TextView
    private lateinit var songAlbumTextView: TextView
    private lateinit var songArtistTextView: TextView
    private lateinit var currentTimeTextView: TextView
    private lateinit var songDurationTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        val bundle = arguments
        val song = Gson().fromJson(bundle?.getString("songJson"), SongEntity::class.java)
        MainActivity.generalUtil.insertImageFromUri(Uri.parse(song.albumArtUriString), albumArt, requireActivity(), null)


    }

    private fun initViews(view: View) {
        albumArt = view.findViewById(R.id.imageview_art_player)
        playButton = view.findViewById(R.id.button_play_player)
        shuffleButton = view.findViewById(R.id.button_shuffle_player)
        fastForwardButton = view.findViewById(R.id.button_fast_foward_player)
        skipForwardButton  = view.findViewById(R.id.button_skip_forward_player)
        previousButton   = view.findViewById(R.id.button_previous_player)
        rewindButton  = view.findViewById(R.id.button_rewind_player)
        sleepTimerButton   = view.findViewById(R.id.button_sleep_timer_player)
        repeatButton   = view.findViewById(R.id.button_repeat_player)
        songSeekBar  = view.findViewById(R.id.seekbar_song_player)
        songNameTextView   = view.findViewById(R.id.textview_song_name_player)
        songAlbumTextView   = view.findViewById(R.id.textview_album_name_player)
        songArtistTextView  = view.findViewById(R.id.textview_artist_player)
        currentTimeTextView  = view.findViewById(R.id.textview_song_current_time_player)
        songDurationTextView  = view.findViewById(R.id.textview_song_total_time_player)
    }
}
