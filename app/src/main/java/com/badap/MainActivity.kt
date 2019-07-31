package com.badap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.badap.fragments.ArtistsFragment
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException


class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermissions()
        initializeFfmpeg()

        val artistFragment = ArtistsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_container, artistFragment).commit()
    }

    private fun setupPermissions() {
        val readExStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val netPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        )
        val writeExStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val granted = PackageManager.PERMISSION_GRANTED

        if (readExStoragePermission != granted ||
            netPermission != granted ||
            writeExStoragePermission != granted
        ) {
            makeRequest()
        } else {
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_LONG).show()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isEmpty() ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "The app can't run if you don't accept dumbass", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Nice", Toast.LENGTH_LONG).show()
                    finish()
                    startActivity(intent)
                }
            }
        }
    }

    private fun initializeFfmpeg() {
        val ffmpeg = FFmpeg.getInstance(this)
        try {
            ffmpeg.loadBinary(object : LoadBinaryResponseHandler() {

                override fun onStart() {
                    Log.i("FFMpeg", "FFMpeg binary loading started")
                }

                override fun onFailure() {
                    Log.e("FFMpeg", "FFMpeg binary failed to load")
                }

                override fun onSuccess() {
                    Log.i("FFMpeg", "FFMpeg binary loaded successfully")
                }

                override fun onFinish() {
                    Log.i("FFMpeg", "FFMpeg binary load finished")
                }
            })
        } catch (e: FFmpegNotSupportedException) {
            // Handle if FFmpeg is not supported by device
            Log.e("FFMpeg", "FFMpeg not supported on device")
        }
    }

}
