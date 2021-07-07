package com.example.shoppingtesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.response.ImageResponse
import com.example.shoppingtesting.other.Constants
import com.example.shoppingtesting.other.Event
import com.example.shoppingtesting.other.Resource
import com.example.shoppingtesting.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ShoppingRepository
): ViewModel() {

    val shoppingItems = repository.observeAllShoppingItem()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images : LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl : LiveData<String> = _curImageUrl

    private val _currentItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val currentItemStatus : LiveData<Event<Resource<ShoppingItem>>> = _currentItemStatus


    fun setImageUrl(url : String){
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemInDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name:String, amountString:String, priceStarting:String){
        if (name.isEmpty() || amountString.isEmpty() || priceStarting.isEmpty()){
            _currentItemStatus.postValue(Event(Resource.error("The fields must not be empty",null)))
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH){
            _currentItemStatus.postValue(Event(Resource.error("The name should not be longer tha ${Constants.MAX_NAME_LENGTH} characters",null)))
            return
        }
        if (priceStarting.length > Constants.MAX_PRICE_LENGTH){
            _currentItemStatus.postValue(Event(Resource.error("The price should not be longer tha ${Constants.MAX_PRICE_LENGTH} characters",null)))
            return
        }
        val amount = try {
            amountString.toInt()
        }catch (e:Exception){
            _currentItemStatus.postValue(Event(Resource.error("Amount is not valid",null)))
            return
        }
        val shoppingItem = ShoppingItem(name,amount,priceStarting.toFloat(),_curImageUrl.value?:"")
        insertShoppingItemInDb(shoppingItem)
        setImageUrl("")
        _currentItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchImageUrl(url:String){
        if (url.isEmpty()){
            return
        }
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(url)
            _images.value = Event(response)
        }
    }
}