package com.example.mystoryapps.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.mystoryapps.databinding.ActivityDetailBinding
import com.example.mystoryapps.utils.Utils


class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getDetailStories()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getDetailStories() {
        val name = intent.getStringExtra(EXTRA_NAME)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val date = intent.getStringExtra(EXTRA_DATE)
        val image = intent.getStringExtra(EXTRA_IMAGE)

        Glide.with(this)
            .load(image)
            .into(binding.ivDetailPhoto)

        binding.apply {
            tvDetailUsername.text = name
            tvDetailDesc.text = desc
            tvDetailDatePost.text = Utils.dateFormat(date)
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_IMAGE = "extra_image"
    }

}