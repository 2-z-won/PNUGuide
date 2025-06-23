package com.pnu.pnuguide.ui

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import androidx.core.content.ContextCompat
import com.pnu.pnuguide.R
import com.pnu.pnuguide.ui.map.MapActivity
import com.pnu.pnuguide.ui.SettingsActivity
import com.pnu.pnuguide.ui.LoginActivity

fun MaterialToolbar.setupHeader1(activity: AppCompatActivity, titleResId: Int) {
    activity.setSupportActionBar(this)
    this.title = activity.getString(titleResId)
    this.navigationIcon = ContextCompat.getDrawable(activity, R.drawable.ic_map)
    this.setNavigationOnClickListener {
        activity.startActivity(Intent(activity, MapActivity::class.java))
    }

    // 메뉴 인플레이트
    this.inflateMenu(R.menu.menu_home_toolbar)

    // 아이콘을 벡터 자산으로 교체
    menu.findItem(R.id.action_settings)?.apply {
        icon = ContextCompat.getDrawable(context, R.drawable.ic_settings)
        setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }
    menu.findItem(R.id.action_logout)?.apply {
        icon = ContextCompat.getDrawable(context, R.drawable.ic_logout)
        setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    // 클릭 리스너
    this.setOnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.action_settings -> {
                activity.startActivity(Intent(activity, SettingsActivity::class.java))
                true
            }
            R.id.action_logout -> {
                com.pnu.pnuguide.data.AuthRepository.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
                true
            }
            else -> false
        }
    }
}

fun MaterialToolbar.setupHeader1(activity: AppCompatActivity, title: String) {
    activity.setSupportActionBar(this)
    this.title = title
    this.navigationIcon = ContextCompat.getDrawable(activity, R.drawable.ic_map)
    this.setNavigationOnClickListener {
        activity.startActivity(Intent(activity, MapActivity::class.java))
    }

    // 메뉴 인플레이트
    this.inflateMenu(R.menu.menu_home_toolbar)

    // 아이콘을 벡터 자산으로 교체
    menu.findItem(R.id.action_settings)?.apply {
        icon = ContextCompat.getDrawable(context, R.drawable.ic_settings)
        setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }
    menu.findItem(R.id.action_logout)?.apply {
        icon = ContextCompat.getDrawable(context, R.drawable.ic_logout)
        setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    // 클릭 리스너
    this.setOnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.action_settings -> {
                activity.startActivity(Intent(activity, SettingsActivity::class.java))
                true
            }
            R.id.action_logout -> {
                com.pnu.pnuguide.data.AuthRepository.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
                true
            }
            else -> false
        }
    }
}

fun MaterialToolbar.setupHeaderSettings(activity: AppCompatActivity, titleResId: Int) {
    activity.setSupportActionBar(this)
    this.title = activity.getString(titleResId)
    this.navigationIcon = ContextCompat.getDrawable(activity, R.drawable.ic_settings)
    this.setNavigationOnClickListener {
        activity.startActivity(Intent(activity, SettingsActivity::class.java))
    }
}

fun MaterialToolbar.setupHeader2(activity: AppCompatActivity, titleResId: Int) {
    activity.setSupportActionBar(this)
    this.title = activity.getString(titleResId)
    this.navigationIcon = ContextCompat.getDrawable(activity, R.drawable.ic_arrow_back_black_24)
    this.setNavigationOnClickListener { activity.finish() }
}
