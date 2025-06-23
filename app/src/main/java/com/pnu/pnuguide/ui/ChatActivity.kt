package com.pnu.pnuguide.ui.chat

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pnu.pnuguide.R
import com.pnu.pnuguide.MainActivity
import com.pnu.pnuguide.ui.course.CourseActivity
import com.pnu.pnuguide.ui.stamp.StampActivity
import com.pnu.pnuguide.ui.setupHeader1
import com.pnu.pnuguide.ui.chat.ChatAdapter
import com.pnu.pnuguide.ui.chat.ChatViewModel
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.collectLatest

class ChatActivity : AppCompatActivity() {

    private val viewModel by viewModels<ChatViewModel> {
        AndroidViewModelFactory.getInstance(application)
    }
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_chat)
        toolbar.setupHeader1(this, R.string.title_chatbot)

        adapter = ChatAdapter()
        val recycler = findViewById<RecyclerView>(R.id.recycler_chat)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val edit = findViewById<EditText>(R.id.edit_message)
        findViewById<Button>(R.id.button_send).setOnClickListener {
            val text = edit.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(text)
                edit.setText("")
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.messages.collectLatest { adapter.submitList(it) }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
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
                R.id.nav_chat -> true
                else -> false
            }
        }
        bottomNav.menu.findItem(R.id.nav_chat).isChecked = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home_toolbar, menu)
        menu.findItem(R.id.action_settings)?.apply {
            icon = ContextCompat.getDrawable(this@ChatActivity, R.drawable.ic_settings)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return true
    }
}
