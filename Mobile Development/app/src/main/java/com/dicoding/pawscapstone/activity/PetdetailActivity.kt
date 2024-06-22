package com.dicoding.pawscapstone.activity

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dicoding.pawscapstone.R

class PetdetailActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var popupText: TextView
    private lateinit var imgPetDetail: ImageView
    private lateinit var petAge: TextView
    private lateinit var petGender: TextView
    private lateinit var petTypeLabel: TextView
    private lateinit var petBreed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petdetail)

        // Binding views
        backButton = findViewById(R.id.back_button_popup)
        popupText = findViewById(R.id.popup_text)
        imgPetDetail = findViewById(R.id.imgPetDetail)
        petAge = findViewById(R.id.pet_age)
        petGender = findViewById(R.id.pet_gender)
        petTypeLabel = findViewById(R.id.pet_type_label)
        petBreed = findViewById(R.id.pet_breed)

        // Set back button click listener
        backButton.setOnClickListener {
            onBackPressed() // Implement back navigation
        }

        // Retrieve intent data
        val petName = intent.getStringExtra("pet_name")
        val petImageUri = intent.getStringExtra("pet_image_uri")
        val petImageResource = intent.getIntExtra("pet_image_resource", -1)
        val petAgeStr = intent.getStringExtra("pet_age")
        val petGenderStr = intent.getStringExtra("pet_gender")
        val petType = intent.getStringExtra("pet_type")
        val petBreedStr = intent.getStringExtra("pet_breed")

        // Update UI with retrieved data
        popupText.text = petName
        // Load image using Glide or similar library
        if (petImageUri != null) {
            Glide.with(this)
                .load(Uri.parse(petImageUri))
                .transform(CenterCrop(), RoundedCorners(150))
                .into(imgPetDetail)
        } else if (petImageResource != -1) {
            Glide.with(this)
                .load(petImageResource)
                .transform(CenterCrop(), RoundedCorners(150))
                .into(imgPetDetail)
        }
        petAge.text = "Pet's date of birth: $petAgeStr"
        petGender.text = "Pet's gender: $petGenderStr"
        petTypeLabel.text = "Pet Type: $petType"
        petBreed.text = "Pet's breed: $petBreedStr"
    }
}