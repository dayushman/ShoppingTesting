package com.example.shoppingtesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.example.shoppingtesting.MainCoroutineRule
import com.example.shoppingtesting.other.Constants
import com.example.shoppingtesting.other.Status
import com.example.shoppingtesting.repositories.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MainViewModelTest{
    lateinit var viewModel: MainViewModel

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp(){
        viewModel = MainViewModel(FakeRepository())
    }


    @Test
    fun `insert shopping item with empty field, returns error`(){
        viewModel.insertShoppingItem("Name","","2.0")

        val value = viewModel.currentItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name, returns error`(){

        val string = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH+1)
                append(1)
        }
        viewModel.insertShoppingItem(string,"5","2.0")

        val value = viewModel.currentItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, returns error`(){

        val string = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH+1)
                append(1)
        }
        viewModel.insertShoppingItem("name","5",string)

        val value = viewModel.currentItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`(){

        viewModel.insertShoppingItem("name","999999999999999999999999","2.0")

        val value = viewModel.currentItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, returns success`(){


        viewModel.insertShoppingItem("name","5","2.0")

        val value = viewModel.currentItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}