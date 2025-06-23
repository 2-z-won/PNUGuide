package com.pnu.pnuguide.ui.course

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.pnu.pnuguide.R
import com.pnu.pnuguide.ui.setupHeader2

class PopularActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_popular)
        toolbar.setupHeader2(this, R.string.title_popular)
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
                "Campus Highlights Tour",
                "Approx. 2 hours",
                imageRes = R.drawable.museum
            ),
            CourseItem(
                "Historic Landmarks Tour",
                "Approx. 3 hours",
                "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/bnWyQkDlsL/hc760uhy_expires_30_days.png"
            ),
            CourseItem(
                "Nature Discovery Walk",
                "Approx. 1.5 hours",
                "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/bnWyQkDlsL/yb4j87iw_expires_30_days.png"
            )
        )
        adapter.submitList(items)
    }
}
