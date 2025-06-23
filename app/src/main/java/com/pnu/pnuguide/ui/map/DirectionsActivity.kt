package com.pnu.pnuguide.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.pnu.pnuguide.R
import com.pnu.pnuguide.ui.setupHeader2

class DirectionsActivity : AppCompatActivity() {

    private lateinit var editStart: EditText
    private lateinit var editDestination: EditText

    private var destLat: Double = Double.NaN
    private var destLng: Double = Double.NaN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directions)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_directions)
        toolbar.setupHeader2(this, R.string.get_directions)
        toolbar.setNavigationOnClickListener { finish() }

        editStart = findViewById(R.id.edit_start)
        editDestination = findViewById(R.id.edit_destination)

        val destName = intent.getStringExtra(EXTRA_DEST_NAME) ?: ""
        destLat = intent.getDoubleExtra(EXTRA_DEST_LATITUDE, Double.NaN)
        destLng = intent.getDoubleExtra(EXTRA_DEST_LONGITUDE, Double.NaN)

        editStart.setText(getString(R.string.my_location))
        editDestination.setText(destName)

        findViewById<MaterialButton>(R.id.button_open_maps).setOnClickListener {
            openGoogleMaps()
        }

        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        }
    }

    private fun openGoogleMaps() {
        if (destLat.isNaN() || destLng.isNaN()) return
        val gmmIntentUri = Uri.parse("google.navigation:q=$destLat,$destLng")
        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    companion object {
        const val EXTRA_DEST_NAME = "extra_dest_name"
        const val EXTRA_DEST_LATITUDE = "extra_dest_latitude"
        const val EXTRA_DEST_LONGITUDE = "extra_dest_longitude"
        private const val LOCATION_PERMISSION_REQUEST = 2
    }
}
