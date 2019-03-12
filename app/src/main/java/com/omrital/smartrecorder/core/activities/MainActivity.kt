package com.omrital.smartrecorder.core.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.omrital.smartrecorder.R
import com.omrital.smartrecorder.core.services.RecorderService
import kotlinx.android.synthetic.main.activity_main.*
import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.Observer
import com.omrital.smartrecorder.BuildConfig

private const val REQUEST_CODE_RECORD_AUDIO = 123

class MainActivity : AppCompatActivity() {

    private lateinit var model: RecordViewModel
    private lateinit var recordListAdapter: RecordListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startRecordService()
        initViewModel()
        initView()
    }

    private fun startRecordService() {
        val recorderServiceIntent = Intent(this, RecorderService::class.java)
        startService(recorderServiceIntent)
    }

    private fun initView() {
        setContentView(R.layout.activity_main)
        initButtons()
        initRecordList()
    }

    private fun initViewModel() {
        model = ViewModelProviders.of(this).get(RecordViewModel::class.java)

        model.getRecords().observe(this, Observer<List<Record>>{ records ->
            records?.let {
                recordListAdapter.setRecords(records)
            }
        })
        model.getAlertMessage().observe(this, Observer { alertMessage ->
            alertMessage?.let {
                showAlert(alertMessage)
            }
        })
    }

    private fun initRecordList() {
        recordListAdapter = RecordListAdapter(this, model, listOf())
        viewManager = LinearLayoutManager(this)

        recordsRecycler.apply {
            adapter = recordListAdapter
            layoutManager = viewManager
        }
    }

    private fun initButtons() {
        startRecordButton.setOnClickListener {
            onStartRecordClick()
        }
        stopRecordButton.setOnClickListener {
            onEndRecordClick()
        }
        stopPlayingRecordButton.setOnClickListener {
            model.stopPlayingRecord()
        }
    }

    private fun onStartRecordClick() {
        if(!isRecordPermissionGranted()) {
            checkForRecordPermission()
            return
        }
        model.startRecording()
    }

    private fun onEndRecordClick() {
        model.stopRecording()
    }

    private fun checkForRecordPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE_RECORD_AUDIO)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_RECORD_AUDIO -> {
                if (isPermissionGranted(grantResults)) {
                    model.startRecording()
                }
            }
        }
    }

    private fun isPermissionGranted(grantResults: IntArray): Boolean {
        return (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
    }

    private fun isRecordPermissionGranted(): Boolean {
         return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun showAlert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
