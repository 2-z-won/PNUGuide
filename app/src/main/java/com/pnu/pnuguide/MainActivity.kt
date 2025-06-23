package com.pnu.pnuguide

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pnu.pnuguide.ui.SettingsActivity
import com.pnu.pnuguide.ui.setupHeader2
import com.pnu.pnuguide.ui.home.HomeFragment
import com.pnu.pnuguide.ui.map.MapActivity
import com.pnu.pnuguide.ui.chat.ChatActivity
import com.pnu.pnuguide.ui.course.CourseActivity
import com.pnu.pnuguide.ui.stamp.StampActivity
import androidx.core.content.ContextCompat
import com.pnu.pnuguide.ui.profile.ProfileFragment
import com.pnu.pnuguide.ui.chat.ChatFragment
import com.pnu.pnuguide.ui.LoginActivity
import com.pnu.pnuguide.data.AuthRepository

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.action_logout -> {
                    AuthRepository.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    toolbar.title = getString(R.string.title_home)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    toolbar.setNavigationIcon(R.drawable.ic_map)
                    toolbar.setNavigationOnClickListener {
                        startActivity(Intent(this, MapActivity::class.java))
                    }
                    supportFragmentManager.commit {
                        replace(R.id.fragment_container, HomeFragment())
                    }
                    true
                }
                R.id.nav_course -> {
                    startActivity(Intent(this, CourseActivity::class.java))
                    true
                }
                R.id.nav_stamp -> {
                    startActivity(Intent(this, StampActivity::class.java))
                    true
                }
                R.id.nav_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home_toolbar, menu)
        menu.findItem(R.id.action_settings)?.apply {
            icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_settings)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return true
    }
}
