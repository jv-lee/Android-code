package com.lee.api.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * workManager 任务类
 * @author jv.lee
 * @date 2021/2/24
 */
class TaskWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private val TAG: String = TaskWorker::class.java.simpleName

    override fun doWork(): Result {
        val code = 1
        Log.i(TAG, "doWork: task ++ ")

        return when (code) {
            1 -> Result.success()
            2 -> Result.failure()
            3 -> Result.retry()
            else -> return Result.success()
        }
    }

}