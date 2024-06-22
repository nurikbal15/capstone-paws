package com.dicoding.pawscapstone.database

import PeliharaanApi
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeliharaanRepository(application: Application) {
    private val peliharaanApi: PeliharaanApi = RetrofitClient.instance.create(PeliharaanApi::class.java)
    private val _allPeliharaan = MutableLiveData<List<Peliharaan>>()
    val allPeliharaan: LiveData<List<Peliharaan>> get() = _allPeliharaan

    init {
        fetchAllPeliharaan()
    }

    private fun fetchAllPeliharaan() {
        peliharaanApi.getAllPeliharaan().enqueue(object : Callback<List<Peliharaan>> {
            override fun onResponse(call: Call<List<Peliharaan>>, response: Response<List<Peliharaan>>) {
                if (response.isSuccessful) {
                    _allPeliharaan.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Peliharaan>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun createPeliharaan(peliharaan: Peliharaan) {
        peliharaanApi.createPeliharaan(peliharaan).enqueue(object : Callback<Peliharaan> {
            override fun onResponse(call: Call<Peliharaan>, response: Response<Peliharaan>) {
                fetchAllPeliharaan()
            }

            override fun onFailure(call: Call<Peliharaan>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun updatePeliharaan(id: Int, peliharaan: Peliharaan) {
        peliharaanApi.updatePeliharaan(id, peliharaan).enqueue(object : Callback<Peliharaan> {
            override fun onResponse(call: Call<Peliharaan>, response: Response<Peliharaan>) {
                fetchAllPeliharaan()
            }

            override fun onFailure(call: Call<Peliharaan>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun deletePeliharaan(id: Int) {
        peliharaanApi.deletePeliharaan(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                fetchAllPeliharaan()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure
            }
        })
    }
}