package com.pnu.pnuguide.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pnu.pnuguide.ui.LoginActivity
import com.pnu.pnuguide.MainActivity
import com.pnu.pnuguide.databinding.ActivitySignUpBinding
import com.pnu.pnuguide.ui.auth.AuthViewModel
import com.pnu.pnuguide.ui.auth.AuthViewModel.AuthState
import kotlinx.coroutines.flow.collect

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignUp.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString()
            val confirm = binding.editConfirmPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.signUp(email, password)
        }



        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is AuthState.Success -> {
                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                        finish()
                    }
                    is AuthState.Error -> {
                        Toast.makeText(this@SignUpActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}
