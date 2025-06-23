package com.pnu.pnuguide.ui.course

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnu.pnuguide.data.AuthRepository
import com.pnu.pnuguide.data.StampRepository
import kotlinx.coroutines.launch

class SpotDetailViewModel(
    private val authRepository: AuthRepository = AuthRepository,
    private val stampRepository: StampRepository = StampRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            try {
                authRepository.signInAnonymously()
            } catch (_: Exception) {
                // handle error in UI if needed
            }
        }
    }

    fun openYoutube(context: Context, videoId: String) {
        viewModelScope.launch {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
            try {
                context.startActivity(appIntent)
            } catch (e: Exception) {
                try {
                    context.startActivity(webIntent)
                } catch (ex: Exception) {
                    AlertDialog.Builder(context)
                        .setMessage("Unable to open video")
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
            }
        }
    }

    fun addStamp(spotId: String) {
        viewModelScope.launch {
            try {
                val uid = authRepository.uid ?: return@launch
                stampRepository.addSpotToStamps(uid, spotId)
            } catch (_: Exception) {
                // error handling left to UI
            }
        }
    }
}
