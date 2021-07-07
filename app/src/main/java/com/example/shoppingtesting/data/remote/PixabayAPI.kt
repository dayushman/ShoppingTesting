package com.example.shoppingtesting.data.remote

import com.example.shoppingtesting.BuildConfig
import com.example.shoppingtesting.data.remote.response.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q")searchQuery: String,
        @Query("key")apiKey : String = BuildConfig.API_KEY
    ): Response<ImageResponse>
}