package com.example.mystoryapps.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mystoryapps.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

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
            //Login
        }

        binding.tvRegisterShortcut.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}