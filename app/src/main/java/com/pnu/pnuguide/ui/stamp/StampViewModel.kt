package com.pnu.pnuguide.ui.stamp

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pnu.pnuguide.data.CourseData
import com.pnu.pnuguide.data.MLImageHelper
import com.pnu.pnuguide.data.Stamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class StampViewModel(private val app: Application) : AndroidViewModel(app) {

    private val prefs = app.getSharedPreferences("stamps", 0)

    private val _stamps = MutableLiveData<List<Stamp>>(loadStamps())
    val stamps: LiveData<List<Stamp>> = _stamps

    private val _label = MutableLiveData<String?>(null)
    val label: LiveData<String?> = _label

    private fun loadStamps(): List<Stamp> {
        val fromCourse = CourseData.loadStamps()
        return fromCourse.mapIndexed { index, stamp ->
            val collected = prefs.getBoolean(stamp.id, false)
            stamp.copy(collected = collected)
        }
    }

    private fun saveStamp(id: String) {
        prefs.edit().putBoolean(id, true).apply()
    }

    fun processImage(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            processBitmap(bitmap)
        }
    }

    fun processBitmap(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            // Use MLImageHelper to classify the captured photo with the
            // TensorFlow Lite model bundled in assets.
            val match = MLImageHelper.classifyImage(app, bitmap)
            if (match != null) {
                _label.postValue(match)
                markStamp(match)
            } else {
                _error.postValue(true)
            }
        }
    }

    private fun markStamp(id: String) {
        val current = _stamps.value?.map {
            if (it.id == id) {
                saveStamp(id)
                it.copy(collected = true)
            } else it
        } ?: emptyList()
        _stamps.postValue(current)
    }

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean> = _error

    fun clearError() { _error.value = false }

    fun clearLabel() { _label.value = null }
}
