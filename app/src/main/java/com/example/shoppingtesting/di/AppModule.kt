package com.example.shoppingtesting.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppingtesting.data.local.ShoppingDao
import com.example.shoppingtesting.data.local.ShoppingItemDB
import com.example.shoppingtesting.data.remote.PixabayAPI
import com.example.shoppingtesting.other.Constants.BASE_URL
import com.example.shoppingtesting.other.Constants.DATABASE_NAME
import com.example.shoppingtesting.repositories.DefaultShoppingRepositories
import com.example.shoppingtesting.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,ShoppingItemDB::class.java,DATABASE_NAME).build()


    @Singleton
    @Provides
    fun provideShoppingItemDAO(
        database: ShoppingItemDB
    ) = database.shoppingDao()


    @Singleton
    @Provides
    fun providePixabayAPI() : PixabayAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideDefaultShoppingRepository(
        dao: ShoppingDao,
        api: PixabayAPI
    ) = DefaultShoppingRepositories(dao,api) as ShoppingRepository
}