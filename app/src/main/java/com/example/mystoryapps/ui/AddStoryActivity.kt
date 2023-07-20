package com.example.mystoryapps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mystoryapps.R
import com.example.mystoryapps.databinding.ActivityAddStoryBinding
import com.example.mystoryapps.utils.Result
import com.example.mystoryapps.utils.Utils.createCustomTempFile
import com.example.mystoryapps.utils.Utils.reduceFileImage
import com.example.mystoryapps.utils.Utils.rotateBitmap
import com.example.mystoryapps.utils.Utils.uriToFile
import com.example.mystoryapps.viewmodel.AddStoryViewModel
import com.example.mystoryapps.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentPhotoPath: String
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var getFile: File? = null
    private var latLng: LatLng? = null
    private var userLocation: Location? = null

    private val addStoryViewModel: AddStoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this@AddStoryActivity,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setActionForButton()
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setActionForButton() {
        binding.btOpenCamera.setOnClickListener {
            startCamera()
        }

        binding.btOpenFile.setOnClickListener {
            openFile()
        }

        binding.btUpload.setOnClickListener {
            uploadStory()
        }

        binding.switchLocation.setOnCheckedChangeListener { _, isCheked ->
            if (isCheked) {
                getLocation()
            } else {
                userLocation = null
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        )
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.example.mystoryapps",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun openFile() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(
            intent,
            "Pilih gambar"
        )
        launcherIntentFile.launch(chooser)
    }

    private fun uploadStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val descriptionText = binding.edtAddDesc.editText?.text

            if (!descriptionText.isNullOrEmpty()) {
                val description = descriptionText.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                if (binding.switchLocation.isChecked) getLocation()

                addStoryViewModel.addNewStory(imageMultipart, description, latLng).observe(this) {
                    if (it != null) {
                        when (it) {
                            is Result.Success -> {
                                binding.progressBar.visibility = View.VISIBLE
                                Toast.makeText(this@AddStoryActivity, it.data.message, Toast.LENGTH_LONG).show()
                                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.GONE
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this@AddStoryActivity, it.error, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, R.string.masukan_deskripsi, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.masukan_foto, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latLng = LatLng(location.latitude, location.longitude)
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this@AddStoryActivity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentFile = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImage: Uri = it.data?.data as Uri
            val myFile = uriToFile(
                selectedImage,
                this@AddStoryActivity
            )
            getFile = myFile

            binding.addImg.setImageURI(selectedImage)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(
                    getFile?.path,
                )
            )
            binding.addImg.setImageBitmap(result)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getLocation()
            }

            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getLocation()
            }

            else -> {
                binding.switchLocation.isChecked = false
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}