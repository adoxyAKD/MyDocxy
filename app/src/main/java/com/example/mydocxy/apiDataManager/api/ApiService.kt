package com.example.mydocxy.apiDataManager.api


import com.example.mydocxy.apiDataManager.database.ObjectData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.*
interface ApiService {

    @GET("objects")
    suspend fun getObjects(): Response<List<ObjectData>>

    @PUT("objects/{id}")
    suspend fun updateObject(
        @Path("id") id: String,
        @Body updatedData: Map<String, Any?>
    ): Response<ObjectData>

    @DELETE("objects/{id}")
    suspend fun deleteObject(@Path("id") id: String): Response<Unit>

    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://api.restful-api.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}




// Mapping API JSON
data class ObjectApiResponse(
    val id: String,
    val name: String,
    val data: ObjectDetails?
)

data class ObjectDetails(
    val color: String?,
    val capacity: String?,
    val price: Double?,
    val description: String?
)