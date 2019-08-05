package com.example.workmanagerexample

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class ExampleWorkManager(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        val TAG = "WorkManager"
        val ARG_EXTRA_PARAM = "ARG_EXTRA_PARAM"
        val OUTPUT_DATA_PARAM = "OUTPUT_DATA_PARAM"
    }

    override fun doWork(): Result {
        val data = inputData.getString(ARG_EXTRA_PARAM)

        Thread.sleep(2000)
        Log.d(TAG, "doWork() called , Params : " + data)

        val outputData = sendResultData("Hello I am result from WorkerExample")
        return Result.success(outputData)
    }

    private fun sendResultData(firstData: String): Data {
        return Data.Builder()
            .putString(OUTPUT_DATA_PARAM, firstData)
            .build()
    }
}