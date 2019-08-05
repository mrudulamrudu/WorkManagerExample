package com.example.workmanagerexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerViews()
    }

    private fun registerViews() {
        btnStartWork.setOnClickListener {
            startWork()
        }

        btnStopWork.setOnClickListener {
            stopWork()
        }
    }

    private fun startWork() {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .setRequiresCharging(false).build()

        val data = Data.Builder()
        data.putString(ExampleWorkManager.ARG_EXTRA_PARAM, "This is a String")

        val workerTest = OneTimeWorkRequestBuilder<ExampleWorkManager>()
            .setConstraints(constraints)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(data.build()).build()

        WorkManager.getInstance().enqueue(workerTest)

        WorkManager.getInstance().getWorkInfoByIdLiveData(workerTest.id).observe(this,
            Observer {
                if (it == null) {
                    Log.d(ExampleWorkManager.TAG, "Status Empty")
                } else {
                    val workInfo = it.state.name
                    Log.d(ExampleWorkManager.TAG, "" + workInfo)
                    when (it.state) {
                        WorkInfo.State.RUNNING -> {
                            Log.d(ExampleWorkManager.TAG, "WorkInfo.State.RUNNING")
                        }
                        WorkInfo.State.CANCELLED -> {
                            Log.d(ExampleWorkManager.TAG, "WorkInfo.State.CANCELLED")
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            Log.d(ExampleWorkManager.TAG, "WorkInfo.State.SUCCEEDED")
                            val successOutputData = it.outputData
                            val firstValue = successOutputData.getString(ExampleWorkManager.OUTPUT_DATA_PARAM)
                            Log.d(ExampleWorkManager.TAG, "Success , output: " + firstValue)
                        }
                    }
                }
            })
    }

    private fun stopWork() {
        WorkManager.getInstance().cancelAllWorkByTag(ExampleWorkManager.TAG)
    }
}
