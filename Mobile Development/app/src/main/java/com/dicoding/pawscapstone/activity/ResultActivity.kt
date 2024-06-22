package com.dicoding.pawscapstone.activity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pawscapstone.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data dari Intent
        val imageUri = intent.getParcelableExtra<Uri>("IMAGE_URI")
        val result = intent.getStringExtra("RESULT")
        val confidence = intent.getFloatExtra("CONFIDENCE", 0f)

        // Menampilkan gambar yang diambil
        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }

        // Menampilkan hasil klasifikasi dan confidence
        binding.resultText.text = "Result: $result"
        binding.confidenceText.text = "Confidence: ${String.format("%.2f", confidence * 100)}%"

        // Menambahkan fungsi pada ImageButton untuk kembali ke activity sebelumnya
        binding.backButtonscan.setOnClickListener {
            onBackPressed()
        }
    }
}