package com.pnu.pnuguide.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import com.pnu.pnuguide.ui.chat.ChatUiMessage

object ChatRepository {
    private val firestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = firestoreSettings { isPersistenceEnabled = true }
    }

    suspend fun loadMessages(uid: String): List<ChatUiMessage> {
        return try {
            val snapshot = firestore.collection("chats").document(uid).get().await()
            val list = snapshot.get("messages") as? List<Map<String, Any>> ?: return emptyList()
            list.map {
                val content = it["content"] as? String ?: ""
                val isUser = it["isUser"] as? Boolean ?: false
                ChatUiMessage(content, isUser)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveMessages(uid: String, messages: List<ChatUiMessage>) {
        val list = messages.map { mapOf("content" to it.content, "isUser" to it.isUser) }
        try {
            firestore.collection("chats").document(uid)
                .set(mapOf("messages" to list))
                .await()
        } catch (_: Exception) {
        }
    }

    suspend fun clearMessages(uid: String) {
        try {
            firestore.collection("chats").document(uid).delete().await()
        } catch (_: Exception) {
        }
    }
}

private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { cont.resume(it) {} }
    addOnFailureListener { cont.resumeWithException(it) }
}
