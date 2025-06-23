package com.pnu.pnuguide.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pnu.pnuguide.MainActivity
import com.pnu.pnuguide.databinding.ActivityLoginBinding
import com.pnu.pnuguide.ui.auth.AuthViewModel
import com.pnu.pnuguide.ui.auth.AuthViewModel.AuthState
import com.pnu.pnuguide.ui.signup.SignUpActivity
import kotlinx.coroutines.flow.collect

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.login(email, password)
        }

        binding.buttonSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is AuthState.Success -> {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is AuthState.Error -> {
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}
