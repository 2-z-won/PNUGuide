package com.pnu.pnuguide.ui.course

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import android.net.Uri
import com.pnu.pnuguide.ui.setupHeader2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.pnu.pnuguide.R

class SpotDetailActivity : AppCompatActivity() {

    private val viewModel by viewModels<SpotDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_detail)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_spot_detail)
        toolbar.setupHeader2(this, R.string.app_name)
        toolbar.setNavigationOnClickListener { finish() }

        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val desc = intent.getStringExtra(EXTRA_DESCRIPTION) ?: ""
        val imageRes = intent.getIntExtra(EXTRA_IMAGE_RES, 0)
        val videoId = intent.getStringExtra(EXTRA_VIDEO_ID) ?: ""
        val spotId = intent.getStringExtra(EXTRA_SPOT_ID) ?: ""
        val latitude = intent.getDoubleExtra(EXTRA_LATITUDE, Double.NaN)
        val longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, Double.NaN)

        findViewById<ImageView>(R.id.image_spot).setImageResource(imageRes)
        findViewById<TextView>(R.id.text_title).text = title
        findViewById<TextView>(R.id.text_description).text = desc

        findViewById<MaterialButton>(R.id.button_watch_video).setOnClickListener {
            viewModel.openYoutube(this, videoId)
        }

        findViewById<MaterialButton>(R.id.button_collect_stamp).setOnClickListener {
            viewModel.addStamp(spotId)
        }

        findViewById<MaterialButton>(R.id.button_get_directions).setOnClickListener {
            if (!latitude.isNaN() && !longitude.isNaN()) {
                val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
                val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_IMAGE_RES = "extra_image_res"
        const val EXTRA_VIDEO_ID = "extra_video_id"
        const val EXTRA_SPOT_ID = "extra_spot_id"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
    }
}
