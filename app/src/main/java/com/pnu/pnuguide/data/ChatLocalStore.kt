package com.pnu.pnuguide.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pnu.pnuguide.ui.chat.ChatUiMessage

object ChatLocalStore {
    private const val PREF_NAME = "chat_local"
    private const val KEY_MESSAGES = "messages"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, 0)

    fun loadMessages(context: Context): List<ChatUiMessage> {
        val json = prefs(context).getString(KEY_MESSAGES, null) ?: return emptyList()
        val type = object : TypeToken<List<ChatUiMessage>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun saveMessages(context: Context, messages: List<ChatUiMessage>) {
        val json = Gson().toJson(messages)
        prefs(context).edit().putString(KEY_MESSAGES, json).apply()
    }

    fun clear(context: Context) {
        prefs(context).edit().clear().apply()
    }
}
