package com.example.mystoryapps.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.mystoryapps.databinding.ActivityRegisterBinding
import com.example.mystoryapps.utils.Result
import com.example.mystoryapps.ui.viewmodel.RegisterViewModel
import com.example.mystoryapps.ui.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory(this)
    }

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
            binding.edtRegisterUsername.isErrorEnabled = false
            binding.edtRegisterEmail.isErrorEnabled = false
            val name = binding.edtRegisterUsername.editText?.text.toString()
            val email = binding.edtRegisterEmail.editText?.text.toString()
            val password = binding.edtRegisterPassword.editText?.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, "Masukan username anda", Toast.LENGTH_SHORT)
                        .show()
                }
                email.isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, "Masukan email anda", Toast.LENGTH_SHORT)
                        .show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, "Masukan password anda", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    userRegister(name, email, password)
                }
            }
        }

        binding.tvLoginShortcut.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun userRegister(name: String, email: String, password: String) {
        viewModel.register(name, email, password).observe(this@RegisterActivity) {
            if (it != null) {
                when (it) {

                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        AlertDialog.Builder(this@RegisterActivity).apply {
                            setTitle("Berhasil!")
                            setMessage("Akun berhasil terdaftar. Ayok Login!")
                            setPositiveButton("Lanjut") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@RegisterActivity,
                            it.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

}