package com.example.shoppingtesting.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShoppingDao {

    @Insert
    suspend fun insertShoppingItems(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItems(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM shopping_items")
    fun getAllShoppingItems() : LiveData<List<ShoppingItem>>


    @Query("SELECT SUM(amount*price) FROM shopping_items")
    fun getTotalPrice():LiveData<Float>
}