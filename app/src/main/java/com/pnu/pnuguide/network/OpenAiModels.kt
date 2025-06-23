package com.pnu.pnuguide.network

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatRequest(
    val model: String = "gpt-4",
    val messages: List<ChatMessage>
)

data class ChatChoice(
    val message: ChatMessage
)

data class ChatResponse(
    val choices: List<ChatChoice>
)