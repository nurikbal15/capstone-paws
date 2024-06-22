package com.dicoding.pawscapstone.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "image_uri") private val imageUri: String?,
    @ColumnInfo(name = "image_resource") private val imageResource: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "age") val age: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "breed") val breed: String
) {
    // Primary constructor for URI-based image
    constructor(imageUri: String, name: String, age: String, gender: String, type: String, breed: String)
            : this(0, imageUri, 0, name, age, gender, type, breed)

    // Secondary constructor for resource-based image
    constructor(imageResource: Int, name: String, age: String, gender: String, type: String, breed: String)
            : this(0, null, imageResource, name, age, gender, type, breed)

    // Method to get image URI
    fun getImageUri(): String? {
        return imageUri
    }

    // Method to get image resource
    fun getImageResource(): Int {
        return imageResource
    }
}