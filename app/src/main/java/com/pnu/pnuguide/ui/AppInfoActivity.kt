package com.pnu.pnuguide.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.pnu.pnuguide.BuildConfig
import com.pnu.pnuguide.R

class AppInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_app_info)
        toolbar.setupHeader2(this, R.string.app_info)

        val versionView = findViewById<TextView>(R.id.tv_app_version)
        versionView.text = "Version: ${BuildConfig.VERSION_NAME}"

        findViewById<TextView>(R.id.tv_readme_link).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.readme_url)))
            startActivity(intent)
        }
    }
}
