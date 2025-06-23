package com.pnu.pnuguide.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun chat(@Body request: ChatRequest): ChatResponse
}