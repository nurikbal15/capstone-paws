package com.dicoding.pawscapstone.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pawscapstone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {

    lateinit var editUsername: EditText
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var editConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var btnLogin: Button
    lateinit var progressDialog: ProgressDialog
    lateinit var btnBack : ImageButton

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        editUsername = findViewById(R.id.editTextUsername)
        editEmail = findViewById(R.id.editTextEmail)
        editPassword = findViewById(R.id.editTextPassword)
        editConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        btnRegister = findViewById(R.id.buttonRegister)
        btnLogin = findViewById(R.id.buttonLogin)
        btnBack = findViewById(R.id.backButtonRegister)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        btnBack.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
        btnRegister.setOnClickListener {
            if (editUsername.text.isNotEmpty() && editEmail.text.isNotEmpty() && editPassword.text.isNotEmpty()) {
                if (editPassword.text.toString() == editConfirmPassword.text.toString()) {
                    //Proses setelah berhasil
                    processRegister()
                } else {
                    Toast.makeText(this, "The password did not match!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processRegister() {
        val username = editUsername.text.toString()
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = username
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Register success!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .addOnFailureListener { error2 ->
                            Toast.makeText(
                                this,
                                "Register failed! Please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
            }else{
                    progressDialog.dismiss()
                    Toast.makeText(this, "Register failed! Please try again", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Register failed! Please try again", Toast.LENGTH_SHORT).show()
            }
    }
}