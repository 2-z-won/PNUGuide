package com.pnu.pnuguide.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

object StampRepository {
    private val firestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = firestoreSettings { isPersistenceEnabled = true }
    }

    suspend fun addSpotToStamps(uid: String, spotId: String) {
        val doc = firestore.collection("stamps").document(uid)
        doc.set(
            mapOf("collectedSpots" to FieldValue.arrayUnion(spotId)),
            SetOptions.merge()
        ).await()
    }
}

private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { cont.resume(it) {} }
    addOnFailureListener { cont.resumeWithException(it) }
}
