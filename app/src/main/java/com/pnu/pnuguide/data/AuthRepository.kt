package com.pnu.pnuguide.data

import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

import com.pnu.pnuguide.data.ChatRepository

object AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var uid: String? = null
        private set

    suspend fun signInAnonymously() {
        val result = auth.signInAnonymously().await()
        uid = result.user?.uid
    }

    suspend fun signUp(email: String, password: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        uid = result.user?.uid
    }

    suspend fun login(email: String, password: String) {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        uid = result.user?.uid
    }

    fun signOut() {
        val currentUid = uid
        auth.signOut()
        uid = null
        currentUid?.let { u ->
            CoroutineScope(Dispatchers.IO).launch {
                ChatRepository.clearMessages(u)
            }
        }
    }

    fun currentUser() = auth.currentUser
}

private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { cont.resume(it) {} }
    addOnFailureListener { cont.resumeWithException(it) }
}
