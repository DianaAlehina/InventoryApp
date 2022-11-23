package com.example.inventory

import android.util.Log
import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import com.example.inventory.data.Record

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String, providerName: String, providerEmail: String, providerPhone: String, ): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt(),
            providerName = providerName,
            providerEmail = providerEmail,
            providerPhone = providerPhone
        )
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String,
        providerName: String,
        providerEmail: String,
        providerPhone: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt(),
            providerName = providerName,
            providerEmail = providerEmail,
            providerPhone = providerPhone
            )
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String,
        providerName: String,
        providerEmail: String,
        providerPhone: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount, providerName, providerEmail, providerPhone)
        updateItem(updatedItem)
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    fun addNewItem(item: Item) {
        item.record = Record.file
        insertItem(item)
    }
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String, providerName: String, providerEmail: String, providerPhone: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount, providerName, providerEmail, providerPhone)
        insertItem(newItem)
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String, providerName: String, providerEmail: String, providerPhone: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank() || providerName.isBlank() || providerEmail.isBlank() || providerPhone.isBlank()) {
            return false
        }
        return true
    }

    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }
}

class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}
