package com.badap.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.badap.R
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.*


class SongFragment : Fragment() {

    private lateinit var waveformFolder: File
    private lateinit var originalFilePath: String
    private lateinit var songDuration: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        originalFilePath = bundle?.get("songPath") as String
        songDuration = bundle.get("duration") as String

        val mImageView = view.findViewById<ImageView>(R.id.song_waveform_image)
        getWaveForm(originalFilePath, mImageView)
    }

    private fun configureTempDirectory(suffix: String) : String{
        val mWaveFormFolder = File(requireActivity().cacheDir.absolutePath + "/waveform")
        if(!mWaveFormFolder.exists()) {
            mWaveFormFolder.mkdir()
        }
        else if (mWaveFormFolder.exists() && !mWaveFormFolder.isDirectory) {
            mWaveFormFolder.delete()
            File(requireActivity().cacheDir, "waveform").mkdir()
        }
        this.waveformFolder = mWaveFormFolder
        val tempFile = File.createTempFile("waveform", suffix, mWaveFormFolder)
        return tempFile.absolutePath
    }

    private fun getWaveForm(audioFilePath: String, mImageView: ImageView){
        val filePath = configureTempDirectory(".raw")


        val ffmpeg = FFmpeg.getInstance(requireContext())

        // Get waveform image
        // val stringArray = arrayOf("-y", "-i", audioFilePath, "-filter_complex", "aformat=channel_layouts=mono,showwavespic=s=320x120", filePath)

        // Get raw file for amplitude extraction
        val stringArray = arrayOf("-y", "-i", audioFilePath, "-f", "s8", filePath)


//     "-i \"" + path + "\" -vn -ac 1 -filter:a aresample=myNum -map 0:a -c:a pcm_s16le -f data -"
//        -f s16le -c:a pcm_s16le

        try {
            ffmpeg.execute(stringArray, object : ExecuteBinaryResponseHandler() {
                override fun onStart() { Log.i("FFMPEG", "It's started") }

                override fun onProgress(message: String?) { Log.i("FFMPEG", message) }

                override fun onFailure(message: String?) { Log.i("FFMPEG", message) }

                override fun onSuccess(message: String?) { Log.i("FFMPEG", message) }

                override fun onFinish() {
//                    Glide.with(requireContext())
//                        .load(File(filePath))
//                        .fitCenter()
//                        .transition(DrawableTransitionOptions.withCrossFade())
//                        .into(mImageView)

                    val file = File(filePath)
                    val size = file.length().toInt()
                    val bytes = ByteArray(size)
                    try {
                        val buf = BufferedInputStream(FileInputStream(file))
                        buf.read(bytes, 0, bytes.size)
                        buf.close()
                        processByteToWaveForm(bytes)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
        } catch (e: FFmpegCommandAlreadyRunningException) {
            Log.e("FFMPEG", "FFMpegCommand is already executing")
        }
    }

    private fun processByteToWaveForm(bytes: ByteArray) {
        val waveFormArray = ByteArray(100)
        val interval = bytes.size/100

        var bitCount = 0
        var indexCount = 0
        for (value in bytes) {
            when(bitCount) {
                0 -> {
                    waveFormArray[indexCount] = value
                    bitCount++
                    indexCount++
                }
                interval -> {
                    bitCount = 0
                }
                else -> {
                    bitCount++
                }
            }
        }
        println("FINAL INDEX COUNT: $indexCount")
        for (value in waveFormArray) {
            println(value)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val dir = waveformFolder
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                File(dir, children[i]).delete()
            }
        }
    }
}
