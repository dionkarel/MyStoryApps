package com.example.mystoryapps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.mystoryapps.R
import com.example.mystoryapps.data.entity.StoriesEntity
import com.example.mystoryapps.databinding.ActivityMapsStoriesBinding
import com.example.mystoryapps.utils.Result
import com.example.mystoryapps.viewmodel.MapsStoriesViewModel
import com.example.mystoryapps.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsStoriesActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsStoriesBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mapsStoriesViewModel: MapsStoriesViewModel by viewModels {
        ViewModelFactory(this)
    }

    private val boundsBuilder = LatLngBounds.Builder()
    private val markerHashMap = HashMap<Marker?, StoriesEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsStoriesViewModel.getStoryWithLocation().observe(this) { it ->
            when (it) {
                is Result.Success -> {
                    addManyMarker(it.data.listStory)
                    moveCameraToIncludeMarkers()
                }
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }

        getLocation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getLocation()
            }
        }

    private fun addManyMarker(stories: List<StoriesEntity>) {
        stories.forEach { story ->
            val latLng = LatLng(story.lat as Double, story.lon as Double)
            val marker = mMap.addMarker(MarkerOptions()
                .position(latLng)
                .title(story.name)
                .snippet(story.description)
            )
            boundsBuilder.include(latLng)
            markerHashMap[marker] = story
        }
    }

    private fun moveCameraToIncludeMarkers() {
        val bounds = boundsBuilder.build()
        val padding = 200
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.moveCamera(cameraUpdate)
    }
}