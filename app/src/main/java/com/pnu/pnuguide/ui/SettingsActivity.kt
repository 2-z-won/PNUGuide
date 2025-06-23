package com.pnu.pnuguide.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.pnu.pnuguide.R
import com.pnu.pnuguide.data.UserRepository
import com.pnu.pnuguide.ui.setupHeader2
import com.pnu.pnuguide.ui.AppInfoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_settings)
        toolbar.setupHeader2(this, R.string.settings)

        val nameView = findViewById<TextView>(R.id.tv_settings_name)
        val emailView = findViewById<TextView>(R.id.tv_settings_email)
        updateUserInfo(nameView, emailView)

        setItem(R.id.item_account, R.drawable.ic_profile, getString(R.string.edit_profile))
        setItem(R.id.item_info, android.R.drawable.ic_menu_info_details, "App Information")

        findViewById<View>(R.id.item_info).setOnClickListener {
            startActivity(android.content.Intent(this, AppInfoActivity::class.java))
        }

        findViewById<View>(R.id.item_account).setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val email = user?.email ?: ""
            showEditEmailDialog(email) { newEmail ->
                val uid = user?.uid
                if (uid != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            user.updateEmail(newEmail).await()
                            UserRepository.updateEmail(uid, newEmail)
                            runOnUiThread {
                                updateUserInfo(nameView, emailView)
                                Toast.makeText(this@SettingsActivity, "Email updated", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@SettingsActivity, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateUserInfo(nameView: TextView, emailView: TextView) {
        val email = FirebaseAuth.getInstance().currentUser?.email
        val prefix = email?.substringBefore("@") ?: ""
        nameView.text = prefix
        emailView.text = email ?: ""
    }

    private fun setItem(resId: Int, iconRes: Int, title: String) {
        val view = findViewById<View>(resId)
        view.findViewById<ImageView>(R.id.icon).setImageResource(iconRes)
        view.findViewById<TextView>(R.id.title).text = title
    }

    private fun showEditEmailDialog(currentEmail: String, onUpdate: (String) -> Unit) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_email, null)
        val edit = dialogView.findViewById<EditText>(R.id.edit_email)
        edit.setText(currentEmail)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        dialogView.findViewById<View>(R.id.button_close).setOnClickListener { dialog.dismiss() }
        dialogView.findViewById<View>(R.id.button_update).setOnClickListener {
            val newEmail = edit.text.toString().trim()
            if (newEmail.isNotEmpty() && newEmail != currentEmail) {
                dialog.dismiss()
                onUpdate(newEmail)
            } else {
                Toast.makeText(this, "Enter a new email", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }
}

private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
    kotlinx.coroutines.suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) {} }
        addOnFailureListener { cont.resumeWithException(it) }
    }
