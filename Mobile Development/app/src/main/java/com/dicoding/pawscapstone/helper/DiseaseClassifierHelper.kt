package com.dicoding.pawscapstone.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.dicoding.pawscapstone.ml.ModelvggDisease
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DiseaseClassifierHelper(private val context: Context) {

    fun classifyDisease(imageUri: Uri, callback: (String, Float) -> Unit) {
        try {
            val bitmap = getBitmapFromUri(imageUri)
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
            val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)

            // Memuat model penyakit
            val model = ModelvggDisease.newInstance(context)

            // Membuat input untuk model
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(byteBuffer)

            // Menjalankan inferensi model dan mendapatkan hasil
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            // Mendapatkan hasil prediksi dan confidence
            val confidences = outputFeature0.floatArray
            val maxConfidence = confidences.maxOrNull() ?: 0f
            val resultIndex = confidences.indexOfFirst { it == maxConfidence }
            val result = diseases[resultIndex]

            Log.d("DiseaseClassifierHelper", "Result: $result, Confidence: $maxConfidence")
            Log.d("DiseaseClassifierHelper", "Confidences: ${confidences.joinToString()}")

            callback(result, maxConfidence)

            // Menutup model setelah selesai digunakan
            model.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DiseaseClassifierHelper", "Error classifying image: ${e.message}")
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(1 * 150 * 150 * 3 * 4) // 1 batch, 150 height, 150 width, 3 channels, 4 bytes per float
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(150 * 150)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until 150) {
            for (j in 0 until 150) {
                val value = intValues[pixel++]

                // Normalize the pixel values from 0-255 to 0-1 by dividing by 255.0
                byteBuffer.putFloat(((value shr 16 and 0xFF) / 255.0f))  // Merah
                byteBuffer.putFloat(((value shr 8 and 0xFF) / 255.0f))   // Hijau
                byteBuffer.putFloat(((value and 0xFF) / 255.0f))         // Biru
            }
        }

        Log.d("DiseaseClassifierHelper", "ByteBuffer: ${byteBuffer}")
        return byteBuffer
    }

    companion object {
        private val diseases = arrayOf("flea allergy", "hotspot", "mange", "ringworm") // daftar penyakit
    }
}