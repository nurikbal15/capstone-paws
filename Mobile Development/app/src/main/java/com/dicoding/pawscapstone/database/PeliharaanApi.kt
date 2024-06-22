import com.dicoding.pawscapstone.database.Peliharaan
import retrofit2.Call
import retrofit2.http.*

interface PeliharaanApi {
    @GET("peliharaan")
    fun getAllPeliharaan(): Call<List<Peliharaan>>

    @POST("peliharaan")
    fun createPeliharaan(@Body peliharaan: Peliharaan): Call<Peliharaan>

    @PUT("peliharaan/{id}")
    fun updatePeliharaan(@Path("id") id: Int, @Body peliharaan: Peliharaan): Call<Peliharaan>

    @DELETE("peliharaan/{id}")
    fun deletePeliharaan(@Path("id") id: Int): Call<Void>
}
