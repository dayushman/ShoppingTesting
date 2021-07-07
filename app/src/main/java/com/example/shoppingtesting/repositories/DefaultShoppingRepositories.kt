package com.example.shoppingtesting.repositories

import androidx.lifecycle.LiveData
import com.example.shoppingtesting.data.local.ShoppingDao
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.PixabayAPI
import com.example.shoppingtesting.data.remote.response.ImageResponse
import com.example.shoppingtesting.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultShoppingRepositories @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItems(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItems(shoppingItem)
    }

    override fun observeAllShoppingItem(): LiveData<List<ShoppingItem>> {
        return shoppingDao.getAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.getTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured",null)
            } else{
                Resource.error("An unknown error occured",null)
            }
        }catch (e:Exception){
            Resource.error("Couldn't reach the server. Check your internet connection",null)
        }
    }
}