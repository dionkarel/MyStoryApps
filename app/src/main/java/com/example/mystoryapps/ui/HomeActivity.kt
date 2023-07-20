package com.example.mystoryapps.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mystoryapps.UserPreference
import com.example.mystoryapps.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setAnimation()
        setActionForButton()

    }


    private fun setAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val desc1 = ObjectAnimator.ofFloat(binding.tvDescHome1, View.ALPHA, 1f)
            .setDuration(500)
        val desc2 = ObjectAnimator.ofFloat(binding.tvDescHome2, View.ALPHA, 1f)
            .setDuration(500)
        val btLogin = ObjectAnimator.ofFloat(binding.btLogin, View.ALPHA, 1f)
            .setDuration(500)
        val btRegister = ObjectAnimator.ofFloat(binding.btRegister, View.ALPHA, 1f)
            .setDuration(500)

        val button = AnimatorSet().apply {
            playTogether(btLogin, btRegister)
        }

        AnimatorSet().apply {
            playSequentially(desc1, desc2, button)
            start()
        }
    }

    private fun setActionForButton() {
        binding.btLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

}