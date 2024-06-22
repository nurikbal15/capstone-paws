package com.dicoding.pawscapstone.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.pawscapstone.R
import com.dicoding.pawscapstone.adapter.NewsAdapter
import com.dicoding.pawscapstone.adapter.PetAdapter
import com.dicoding.pawscapstone.database.Pet
import com.dicoding.pawscapstone.databinding.ActivityMainBinding
import com.dicoding.pawscapstone.models.NewsResponse
import com.dicoding.pawscapstone.news.ApiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var petList: MutableList<Pet>
    private lateinit var petAdapter: PetAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var rvFeed: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private val apiKey = "5a8109e0575d4a13ae6a222fbae07410"
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var petImageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private lateinit var btnPhoto: Button

    private val galleryResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                petImageUri = uri
                btnPhoto.text = "Photo Uploaded Successfully"
                btnPhoto.setBackgroundColor(resources.getColor(R.color.green, null))
            }
        }
    }

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            petImageUri = Uri.parse(currentPhotoPath)
            btnPhoto.text = "Photo Uploaded Successfully"
            btnPhoto.setBackgroundColor(resources.getColor(R.color.green, null))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Initialize views
        rvFeed = binding.rvFeed
        progressBar = binding.progressBar

        rvFeed.layoutManager = LinearLayoutManager(this)

        fetchNews()

        // Set up RecyclerView for pet
        petList = mutableListOf()
        petAdapter = PetAdapter(this, petList)

        recyclerView = binding.rvPets
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = petAdapter

        // In onCreate or appropriate place in MainActivity
        petAdapter.setOnItemClickListener { pet ->
            val intent = Intent(this, PetdetailActivity::class.java)
            intent.putExtra("pet_name", pet.name)
            pet.getImageUri()?.let {
                intent.putExtra("pet_image_uri", it)
            } ?: intent.putExtra("pet_image_resource", pet.getImageResource())
            intent.putExtra("pet_age", pet.age) // tambahkan data age
            intent.putExtra("pet_gender", pet.gender) // tambahkan data gender
            intent.putExtra("pet_type", pet.type) // tambahkan data type
            intent.putExtra("pet_breed", pet.breed) // tambahkan data breed
            startActivity(intent)
        }

        bottomNavigationView = binding.menuBar
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.scan -> {
                    startActivity(Intent(this, ScanActivity::class.java))
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

        binding.btnAddPet.setOnClickListener {
            showAddPetPopup()
        }

        binding.btnSeeAll.setOnClickListener {
            showSeeAllPopup()
        }

        setUserDisplayName()

        // Show or hide the "no pets" message
        toggleNoPetsMessage()

    }

    private fun toggleNoPetsMessage() {
        if (petList.isEmpty()) {
            binding.tvNoPetsMessage.visibility = View.VISIBLE
        } else {
            binding.tvNoPetsMessage.visibility = View.GONE
        }
    }

    private fun setUserDisplayName() {
        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        currentUser?.let {
            val displayName = it.displayName
            binding.nameTextView.text = displayName ?: "User"
        }
    }

    private fun fetchNews() {
        // Show the progress bar before starting the network request
        progressBar.visibility = View.VISIBLE
        rvFeed.visibility = View.GONE

        val call = ApiClient.instance.getNews(
            query = "Tips for Cat and Dog Care",
            sortBy = "popularity",
            apiKey = apiKey
        )
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                // Hide the progress bar after data is loaded
                progressBar.visibility = View.GONE
                rvFeed.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val articles = response.body()?.articles?.filter { article ->
                        article.title != "[Removed]" && article.description != "[Removed]"
                    }?.take(6) ?: emptyList()

                    adapter = NewsAdapter(articles)
                    rvFeed.adapter = adapter
                } else {
                    Log.e("fetchNews", "Failed to fetch news: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Hide the progress bar in case of failure as well
                rvFeed.visibility = View.GONE
                Log.e("fetchNews", "Failed to fetch news", t)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showAddPetPopup() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_addpets, null)

        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true)
        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

        val btnSubmit = popupView.findViewById<Button>(R.id.popup_button)
        btnPhoto = popupView.findViewById<Button>(R.id.pet_photo) // Initialize btnPhoto in popup view
        val petName = popupView.findViewById<EditText>(R.id.pet_name)
        val petAge = popupView.findViewById<EditText>(R.id.pet_age)
        val petGender = popupView.findViewById<EditText>(R.id.pet_gender)
        val petBreed = popupView.findViewById<EditText>(R.id.pet_breed)
        val petTypeGroup = popupView.findViewById<RadioGroup>(R.id.toggle_switch)

        btnPhoto.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.menu_photo, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_gallery -> {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryResultLauncher.launch(intent)
                        true
                    }
                    R.id.action_camera -> {
                        dispatchTakePictureIntent()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        btnSubmit.setOnClickListener {
            val name = petName.text.toString()
            val age = petAge.text.toString()
            val gender = petGender.text.toString()
            val breed = petBreed.text.toString()
            val selectedPetType = when (petTypeGroup.checkedRadioButtonId) {
                R.id.rb_cat -> "Cat"
                R.id.rb_dog -> "Dog"
                else -> ""
            }

            if (name.isNotEmpty() && age.isNotEmpty() && gender.isNotEmpty() && breed.isNotEmpty() && selectedPetType.isNotEmpty()) {
                val defaultImageResource = when (selectedPetType) {
                    "Cat" -> R.drawable.ic_cat_default
                    "Dog" -> R.drawable.ic_dog_default
                    else -> R.drawable.ic_placeholder // fallback if necessary
                }

                // Create Pet object based on whether image URI is available
                val newPet = if (petImageUri != null) {
                    Pet(petImageUri.toString(), name, age, gender, selectedPetType, breed)
                } else {
                    Pet(defaultImageResource, name, age, gender, selectedPetType, breed)
                }

                petList.add(newPet)
                petAdapter.notifyDataSetChanged()
                popupWindow.dismiss()

                // Clear petImageUri after use
                petImageUri = null

                // Update the visibility of the "no pets" message
                toggleNoPetsMessage()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        val container = popupWindow.contentView.parent as View
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.5f
        wm.updateViewLayout(container, p)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSeeAllPopup() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_seeall, null)

        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true)
        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

        val rvSeeAll = popupView.findViewById<RecyclerView>(R.id.rvSeeall)
        val tvNoPetsMessage = popupView.findViewById<TextView>(R.id.tvNoPetsMessage)
        rvSeeAll.layoutManager = GridLayoutManager(this, 2)
        rvSeeAll.adapter = petAdapter

        // Show or hide the "no pets" message
        if (petList.isEmpty()) {
            tvNoPetsMessage.visibility = View.VISIBLE
            rvSeeAll.visibility = View.GONE
        } else {
            tvNoPetsMessage.visibility = View.GONE
            rvSeeAll.visibility = View.VISIBLE
        }

        val container = popupWindow.contentView.parent as View
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.5f
        wm.updateViewLayout(container, p)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e(TAG, "Error creating image file", ex)
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.dicoding.pawscapstone.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraResultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}