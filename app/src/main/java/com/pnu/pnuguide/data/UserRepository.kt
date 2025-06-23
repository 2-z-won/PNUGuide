package com.pnu.pnuguide.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

object UserRepository {
    private val firestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = firestoreSettings { isPersistenceEnabled = true }
    }

    suspend fun updateEmail(uid: String, email: String) {
        firestore.collection("users")
            .document(uid)
            .set(mapOf("email" to email), SetOptions.merge())
            .await()
    }
}

private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { cont.resume(it) {} }
    addOnFailureListener { cont.resumeWithException(it) }
}
