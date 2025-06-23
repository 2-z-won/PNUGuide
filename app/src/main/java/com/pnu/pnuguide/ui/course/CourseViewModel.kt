package com.pnu.pnuguide.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {

    private val _courses = MutableStateFlow<List<String>>(emptyList())
    val courses: StateFlow<List<String>> = _courses

    fun loadCourses() {
        viewModelScope.launch {
            // TODO: load from network or Firestore
            _courses.value = listOf("Course A", "Course B")
        }
    }
}