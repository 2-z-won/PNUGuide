package com.pnu.pnuguide.ui.course

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.pnu.pnuguide.R
import com.pnu.pnuguide.ui.setupHeader2

class StudyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_study)
        toolbar.setupHeader2(this, R.string.title_study)
        toolbar.setNavigationOnClickListener { finish() }

        val recycler = findViewById<RecyclerView>(R.id.recycler_course)
        recycler.layoutManager = LinearLayoutManager(this)
        val adapter = CourseListAdapter()
        recycler.adapter = adapter
        adapter.onItemClick = { item ->
            SpotDetailDialogFragment.newInstance(item)
                .show(supportFragmentManager, "detail")
        }

        val items = listOf(
            CourseItem(
                "넉넉한 터",
                "건물번호: 203",
                imageRes = R.drawable.nuck
            ),
            CourseItem(
                "cafe 운죽정",
                "건물번호: 202",
                imageRes = R.drawable.cafe
            ),
            CourseItem(
                "진리의 뜰",
                "운죽정 뒷편",
                imageRes = R.drawable.jinli
            )
        )
        adapter.submitList(items)
    }
}
