package com.omrital.smartrecorder.core.activities

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import com.omrital.smartrecorder.core.utils.FileNameFormatter
import java.io.IOException
import java.text.DateFormat
import java.util.*
import java.io.File
import kotlin.collections.ArrayList

private const val LOG_TAG = "RecordViewModel"
private var IS_RECORDING = false

class RecordViewModel: ViewModel() {
    private var recordsDirectory = "${Environment.getExternalStorageDirectory()}/RecordsSmartRecorder"
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var nowPlayingFile: String? = null

    private val records: MutableLiveData<List<Record>> by lazy {
        MutableLiveData<List<Record>>().also {
            Log.d("omri", "loadRecords")
            loadRecords()
        }
    }

    private val alertMessage: MutableLiveData<String> = MutableLiveData()

    fun getRecords(): LiveData<List<Record>> {
        return records
    }

    fun getAlertMessage(): MutableLiveData<String> {
        return alertMessage
    }

    fun startRecording() {
        if(IS_RECORDING) {
            alertMessage.value = "Already recording"
        } else {
            startNewRecord()
        }
    }

    fun stopRecording() {
        if(IS_RECORDING) {
            stopCurrentRecord()
        } else {
            alertMessage.value = "You have to start a record first"
        }
    }

    fun playRecord(recordName: String) {
        if(nowPlayingFile === recordName) {
            mediaPlayer?.isPlaying?.let {
                if(it) {
                    return
                }
                else mediaPlayer?.start()
            }
        } else {
            playNewRecord(recordName)
        }

    }

    private fun playNewRecord(recordName: String) {
        nowPlayingFile = recordName
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource("$recordsDirectory/$recordName")
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    fun pausePlayingRecord() {
        mediaPlayer?.pause()
    }

    fun stopPlayingRecord() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        nowPlayingFile = null
        mediaPlayer = null
    }

    private fun startNewRecord() {
        IS_RECORDING = true
        alertMessage.value = "start a new record"

        val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
        val fileName = FileNameFormatter.format("record_$currentDateTimeString.3gp");
        val filePath = "$recordsDirectory/$fileName"

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(filePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
            start()
        }
    }

    private fun stopCurrentRecord() {
        IS_RECORDING = false
        alertMessage.value = "Stop current record"

        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        loadRecords()
    }

    private fun loadRecords(): List<Record> {
        var records: ArrayList<Record> = ArrayList()
        val directory = File(recordsDirectory)
        val files = directory.listFiles()

        if(files != null) {
            for (i in files.indices) {
                records.add(Record(files[i].name))
            }
        }
        return records
    }

    private fun release() {
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}