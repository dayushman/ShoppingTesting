package com.example.shoppingtesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.shoppingtesting.getOrAwaitValue
import com.example.shoppingtesting.launchFragmentInHiltContainer
import com.example.shoppingtesting.ui.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dB")
    lateinit var database: ShoppingItemDB
    private lateinit var dao: ShoppingDao

    @Before
    fun setup(){
        hiltRule.inject()
        dao = database.shoppingDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentInHiltContainer<ShoppingFragment>{

        }
    }

    @Test
    fun insertShoppingItemTest() = runBlockingTest {
        val item = ShoppingItem("Item",2,20f,"url",1)
        dao.insertShoppingItems(item)
        val resultItems = dao.getAllShoppingItems().getOrAwaitValue()

        assertThat(resultItems).contains(item)
    }


    @Test
    fun deleteShoppingItems() = runBlockingTest {
        val item = ShoppingItem("Item",2,20f,"url",1)
        dao.insertShoppingItems(item)
        dao.deleteShoppingItems(item)
        val resultItems = dao.getAllShoppingItems().getOrAwaitValue()

        assertThat(resultItems).doesNotContain(item)
    }

    @Test
    fun getTotalPrice() = runBlockingTest {
        val item = ShoppingItem("Item",2,20f,"url",1)
        val item2 = ShoppingItem("Item",2,20f,"url",2)
        val item3 = ShoppingItem("Item",2,20f,"url",3)

        dao.insertShoppingItems(item)
        dao.insertShoppingItems(item2)
        dao.insertShoppingItems(item3)

        val total = dao.getTotalPrice().getOrAwaitValue()

        assertThat(total).isEqualTo(120f)

    }
}