package com.pnu.pnuguide.data

import android.content.Context

object QuizProgress {
    private const val PREF_NAME = "quiz_progress"
    private const val KEY_COUNT = "count"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, 0)

    fun getCount(context: Context): Int = prefs(context).getInt(KEY_COUNT, 0)

    fun isSolved(context: Context, id: String): Boolean =
        prefs(context).getBoolean(id, false)

    fun markSolved(context: Context, id: String) {
        val p = prefs(context)
        if (!p.getBoolean(id, false)) {
            val count = p.getInt(KEY_COUNT, 0) + 1
            p.edit().putBoolean(id, true).putInt(KEY_COUNT, count).apply()
        }
    }
}
