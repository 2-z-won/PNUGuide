package com.pnu.pnuguide.ui.stamp

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnu.pnuguide.data.MLImageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class StampViewModel(private val app: Application) : AndroidViewModel(app) {

    private val prefs = app.getSharedPreferences("stamps", 0)

    private val _stamps = MutableLiveData(loadStamps())
    val stamps: LiveData<List<Boolean>> = _stamps

    private fun loadStamps(): List<Boolean> =
        List(8) { prefs.getBoolean("slot_$it", false) }

    private fun saveStamp(index: Int) {
        prefs.edit().putBoolean("slot_$index", true).apply()
    }

    fun processImage(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val match = MLImageHelper.matchSpot(bitmap)
            if (match != null) {
                markNextStamp()
            } else {
                _error.postValue(true)
            }
        }
    }

    private fun markNextStamp() {
        val current = _stamps.value?.toMutableList() ?: MutableList(8) { false }
        val idx = current.indexOfFirst { !it }
        if (idx != -1) {
            current[idx] = true
            saveStamp(idx)
            _stamps.postValue(current)
        }
    }

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean> = _error

    fun clearError() { _error.value = false }
}
