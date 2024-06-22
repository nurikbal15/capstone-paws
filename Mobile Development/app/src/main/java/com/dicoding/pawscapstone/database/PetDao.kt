package com.dicoding.pawscapstone.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PetDao {
    @Insert
    suspend fun insertPet(pet: Pet)

    @Query("SELECT * FROM pets")
    suspend fun getAllPets(): List<Pet>
}