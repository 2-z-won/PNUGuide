package com.pnu.pnuguide.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pnu.pnuguide.network.ChatMessage
import com.pnu.pnuguide.network.ChatRequest
import com.pnu.pnuguide.network.RetrofitClient
import com.pnu.pnuguide.data.AuthRepository
import com.pnu.pnuguide.data.ChatRepository
import com.pnu.pnuguide.data.ChatLocalStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val app: Application) : AndroidViewModel(app) {

    private val systemPrompt = "부산대학교를 탐방하는 학생들에게 도움을 주는 가이드 챗봇입니다."

    private val _messages = MutableStateFlow<List<ChatUiMessage>>(emptyList())
    val messages: StateFlow<List<ChatUiMessage>> = _messages

    init {
        _messages.value = ChatLocalStore.loadMessages(app)
        AuthRepository.uid?.let { uid ->
            viewModelScope.launch {
                val loaded = ChatRepository.loadMessages(uid)
                _messages.value = loaded
                ChatLocalStore.saveMessages(app, loaded)
            }
        }
    }

    fun sendMessage(text: String) {
        val userMsg = ChatUiMessage(text, true)
        _messages.value = _messages.value + userMsg
        ChatLocalStore.saveMessages(app, _messages.value)
        AuthRepository.uid?.let { uid ->
            viewModelScope.launch { ChatRepository.saveMessages(uid, _messages.value) }
        }
        viewModelScope.launch {
            try {
                val requestMessages = mutableListOf(ChatMessage("system", systemPrompt))
                requestMessages += _messages.value.map {
                    ChatMessage(if (it.isUser) "user" else "assistant", it.content)
                }
                val request = ChatRequest(messages = requestMessages)
                val response = RetrofitClient.openAiService.chat(request)
                val reply = response.choices.firstOrNull()?.message?.content ?: ""
                _messages.value = _messages.value + ChatUiMessage(reply, false)
                ChatLocalStore.saveMessages(app, _messages.value)
                AuthRepository.uid?.let { u ->
                    ChatRepository.saveMessages(u, _messages.value)
                }
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatUiMessage("Error: ${e.message}", false)
            }
        }
    }

    override fun onCleared() {
        ChatLocalStore.saveMessages(app, _messages.value)
        super.onCleared()
    }
}
