package com.pnu.pnuguide.ui.course

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.pnu.pnuguide.R
import com.pnu.pnuguide.ui.setupHeader2

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_history)
        toolbar.setupHeader2(this, R.string.title_history)
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
                "박물관",
                "건물 번호: 412",
                imageRes = R.drawable.museum
            ),
            CourseItem(
                "지질박물관",
                "건물 번호: 414",
                imageRes = R.drawable.jijil
            ),
            CourseItem(
                "중앙 도서관",
                "건물 번호: 510",
                imageRes = R.drawable.ang
            )
        )
        adapter.submitList(items)
    }
}
