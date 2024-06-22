package com.dicoding.pawscapstone.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.pawscapstone.database.Peliharaan
import com.dicoding.pawscapstone.database.PeliharaanRepository

class PeliharaanViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PeliharaanRepository(application)
    val allPeliharaan: LiveData<List<Peliharaan>> = repository.allPeliharaan

    fun createPeliharaan(peliharaan: Peliharaan) {
        repository.createPeliharaan(peliharaan)
    }

    fun updatePeliharaan(id: Int, peliharaan: Peliharaan) {
        repository.updatePeliharaan(id, peliharaan)
    }

    fun deletePeliharaan(id: Int) {
        repository.deletePeliharaan(id)
    }
}