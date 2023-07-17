package com.example.mystoryapps.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mystoryapps.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setAnimation()
        setActionForButton()
    }

    private fun setAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogoRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val desc1 = ObjectAnimator.ofFloat(binding.tvDescRegister1, View.ALPHA, 1f).setDuration(500)
        val tvUsername = ObjectAnimator.ofFloat(binding.tvRegisterUsername, View.ALPHA, 1f).setDuration(500)
        val edtUsername = ObjectAnimator.ofFloat(binding.edtRegisterUsername, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val btReg = ObjectAnimator.ofFloat(binding.btRegister, View.ALPHA, 1f).setDuration(500)

        val username = AnimatorSet().apply {
            playTogether(tvUsername, edtUsername)
        }

        val email = AnimatorSet().apply {
            playTogether(tvEmail, edtEmail)
        }

        val password = AnimatorSet().apply {
            playTogether(tvPassword, edtPassword)
        }

        AnimatorSet().apply {
            playSequentially(desc1, username, email, password, btReg)
            start()
        }
    }

    private fun setActionForButton() {
        binding.btRegister.setOnClickListener {
            //Register
        }

        binding.tvLoginShortcut.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


}