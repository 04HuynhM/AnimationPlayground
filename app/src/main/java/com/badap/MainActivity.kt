package com.badap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.badap.fragments.MenuFragment
import com.badap.utilities.GeneralUtility
import com.badap.utilities.MediaStoreUtility
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        var indexedArtists: ArrayList<Artist>? = null
        var indexedAlbums: ArrayList<Album>? = null
        var indexedSongs: ArrayList<Song>? = null
        var lastUpdated: Date? = null
        val mediaStoreUtil = MediaStoreUtility()
        val generalUtil = GeneralUtility()
    }

    private val PERMISSIONS_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermissions()
        initializeFfmpeg()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main_navigator)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val screenSize = generalUtil.getScreenSize(this)

        if (!preferences.contains("screen_width") || !preferences.contains("screen_height")) {
            preferences.edit {
                putInt("screen_width", screenSize.x)
                putInt("screen_height", screenSize.y)
            }
        }
        getCachedLibrary()

        val menuFragment = MenuFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_container, menuFragment).commit()
    }

    private fun cacheCurrentLibrary() {
        val filePath = getCachedDirectory()

        val fos = FileOutputStream(filePath)
        val oos = ObjectOutputStream(fos)

        val dataArray = arrayOf(indexedArtists, indexedAlbums, indexedSongs)
        oos.writeObject(dataArray)
    }

    private fun getCachedLibrary() {
        val dataFile = getCachedDirectory()

        if (dataFile.exists()) {
            val fis = FileInputStream(dataFile)
            val ois = ObjectInputStream(fis)

            val dataArray = ois.readObject() as Array<ArrayList<*>>
            indexedArtists = dataArray[0] as ArrayList<Artist>
            indexedAlbums = dataArray[1] as ArrayList<Album>
            indexedSongs = dataArray[2] as ArrayList<Song>
        } else {
            initializeLibraryArrays()
        }
    }

    private fun initializeLibraryArrays() {
        val mediaHelper = MediaStoreUtility()
        indexedAlbums = mediaHelper.getAllAlbums(this)
        indexedArtists = mediaHelper.getAllArtists(this)
        indexedSongs = mediaHelper.getAllSongs(this)
        lastUpdated = Date()

        cacheCurrentLibrary()
    }

    private fun getCachedDirectory() : File{
        val indexFolder = File(this.cacheDir.absolutePath + "/library_index")
        if(!indexFolder.exists()) {
            indexFolder.mkdir()
        }
        return File(indexFolder, "data.ser")
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_navigator_music -> {
                    loadFragment(MenuFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_navigator_profile -> {
//                    loadFragment(RunsMenuFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_navigator_friends -> {
//                    loadFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bottom_navigator_search -> {
//                    loadFragment(GroupsFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun loadFragment(fragment: Fragment?) {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit()
        }
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
