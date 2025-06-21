package com.pnu.pnuguide.ui.stamp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.pnu.pnuguide.R
import com.pnu.pnuguide.ui.setupHeader2

class StampActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_stamp)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_stamp)
        toolbar.setupHeader2(this, R.string.title_stamp)
    }
}
