package com.example.shoppingtesting.repositories

import androidx.lifecycle.LiveData
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.response.ImageResponse
import com.example.shoppingtesting.other.Resource
import retrofit2.Response

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItem():LiveData<List<ShoppingItem>>

    fun observeTotalPrice():LiveData<Float>

    suspend fun searchForImage(imageQuery:String):Resource<ImageResponse>

}