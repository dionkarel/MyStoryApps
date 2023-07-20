package com.example.mystoryapps.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.mystoryapps.R
import com.example.mystoryapps.UserPreference
import com.example.mystoryapps.data.entity.UserModel
import com.example.mystoryapps.databinding.ActivityLoginBinding
import com.example.mystoryapps.response.LoginResponse
import com.example.mystoryapps.utils.Result
import com.example.mystoryapps.viewmodel.LoginViewModel
import com.example.mystoryapps.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(this)
    }
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userPreference = UserPreference(this)

        setAnimation()
        setActionForButton()
    }

    private fun setAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogoLogin, View.TRANSLATION_X, -30F, 30F).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvEmail = ObjectAnimator.ofFloat(binding.tvLoginEmail, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtLoginEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvLoginPassword, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtLoginPassword, View.ALPHA, 1f).setDuration(500)
        val btLogin = ObjectAnimator.ofFloat(binding.btLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tvEmail, edtEmail, tvPassword, edtPassword, btLogin)
            start()
        }
    }

    private fun setActionForButton() {
        binding.btLogin.setOnClickListener {
            val email = binding.edtLoginEmail.editText?.text.toString()
            val password = binding.edtLoginPassword.editText?.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edtLoginEmail.error = TextUtils.concat(getString(R.string.masukan_email))
                }
                password.isEmpty() -> {
                    binding.edtLoginPassword.error = TextUtils.concat(getString(R.string.masukan_password))
                }
            }
            loginViewModel.userLogin(email, password).observe(this) { result ->
                if (result != null) {
                    when(result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            loginData(result.data)
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Selamat datang!")
                                setMessage("Mulai bagi pengalamanmu!")
                                setPositiveButton("Lanjut") { _, _ ->
                                    val intent = Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    )
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
        binding.tvRegisterShortcut.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginData(data: LoginResponse) {
        when {
            data.error -> Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
            else -> {
                val userPreference = UserPreference(this)
                val userModel = UserModel(data.loginResult.userId, data.loginResult.name,
                    data.loginResult.token
                )
                userPreference.saveUser(userModel)
                startActivity(Intent(this, MainActivity::class.java))
                this@LoginActivity.finish()
            }
        }
    }

}