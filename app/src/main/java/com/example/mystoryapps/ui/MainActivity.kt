package com.example.mystoryapps.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapps.R
import com.example.mystoryapps.StoriesAdapter
import com.example.mystoryapps.UserPreference
import com.example.mystoryapps.data.entity.StoriesEntity
import com.example.mystoryapps.data.entity.UserModel
import com.example.mystoryapps.databinding.ActivityMainBinding
import com.example.mystoryapps.response.LoginResultResponse
import com.example.mystoryapps.viewmodel.MainViewModel
import com.example.mystoryapps.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    private val storiesAdapter = StoriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionForButton()
        getStories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                UserPreference(this).userLogout()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_maps -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setActionForButton() {
        binding.swipeLayout.setOnRefreshListener {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            binding.swipeLayout.isRefreshing = false
        }
        binding.fbAddStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
        }
    }

    private fun getStories() {
        val layoutManager = LinearLayoutManager(this)
        binding.apply {
            rvStory.layoutManager = layoutManager
            rvStory.setHasFixedSize(true)
            rvStory.adapter = storiesAdapter
        }
        mainViewModel.getStories().observe(
            this@MainActivity
        ) {
            storiesAdapter.submitData(lifecycle, it)
        }

        storiesAdapter.setOnItemClickCallback(object : StoriesAdapter.OnItemClickCallback{
            override fun onItemClicked(story: StoriesEntity) {
                intentToDetail(story)
            }
        })
    }

    private fun intentToDetail(story: StoriesEntity) {
        Intent(this, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_NAME, story.name)
            it.putExtra(DetailActivity.EXTRA_DESC, story.description)
            it.putExtra(DetailActivity.EXTRA_DATE, story.createdAt)
            it.putExtra(DetailActivity.EXTRA_IMAGE, story.photoUrl)
            startActivity(it)
        }
    }

}