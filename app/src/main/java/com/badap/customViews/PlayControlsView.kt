package com.badap.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.badap.R

class PlayControlsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, defStyleRes: Int = 0)
    : LinearLayout(context, attrs, defStyle, defStyleRes) {

    enum class PageType {
        ALL_ALBUMS,
        ALL_ARTISTS,
        ALL_SONGS,
        SINGLE_ARTIST,
        SINGLE_ALBUM
    }

    var playButton: Button
    var shuffleButton: Button

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_play_controls, this, true)
        playButton = findViewById(R.id.custom_play_button)
        shuffleButton = findViewById(R.id.custom_shuffle_button)
        this.requestDisallowInterceptTouchEvent(true)

        shuffleButton.setOnClickListener {
            println("Is anything happening?")
            Toast.makeText(context, "Toastie shuffle", Toast.LENGTH_LONG).show()
        }

        playButton.setOnClickListener {
            println("Is anything happening?")
            Toast.makeText(context, "Toastie Play", Toast.LENGTH_LONG).show()
        }
    }
}