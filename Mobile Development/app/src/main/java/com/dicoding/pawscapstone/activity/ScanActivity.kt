package com.dicoding.pawscapstone.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pawscapstone.R
import com.dicoding.pawscapstone.databinding.ActivityScanBinding
import com.dicoding.pawscapstone.helper.DiseaseClassifierHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null
    private lateinit var diseaseClassifierHelper: DiseaseClassifierHelper
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diseaseClassifierHelper = DiseaseClassifierHelper(this)

        // Set up gallery launcher
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    currentImageUri = uri
                    binding.previewImageView.setImageURI(uri)

                    // Hide the TextViews when image is successfully picked
                    binding.titleTextView.visibility = View.GONE
                    binding.subtitleTextView.visibility = View.GONE
                }
            }
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }

        setupBottomNavigation()
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            try {
                // Show ProgressBar when analysis starts
                binding.progressIndicator.visibility = View.VISIBLE

                // Always classify disease
                diseaseClassifierHelper.classifyDisease(uri) { result, confidence ->
                    // Hide ProgressBar when analysis is done
                    binding.progressIndicator.visibility = View.GONE

                    // Handle the disease classification result
                    moveToResult(uri, result, confidence)
                }
            } catch (e: Exception) {
                // Hide ProgressBar in case of an error
                binding.progressIndicator.visibility = View.GONE
                showToast("Failed to analyze image: ${e.message}")
            }
        } ?: showToast("Please select an image first")
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuBar)
        bottomNavigationView.selectedItemId = R.id.scan // Make sure to set the correct id for the menu item

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.scan -> true
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.reminder -> {
                    startActivity(Intent(this, ReminderActivity::class.java))
                    true
                }
                R.id.account -> {
                    startActivity(Intent(this, AccountActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun moveToResult(imageUri: Uri, result: String, confidence: Float) {
        // Implement navigation to the result activity with the classification result
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("IMAGE_URI", imageUri)
            putExtra("RESULT", result)
            putExtra("CONFIDENCE", confidence)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}