package com.pnu.pnuguide.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class FirestoreSyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        // TODO: sync pending firestore operations
        return Result.success()
    }
}