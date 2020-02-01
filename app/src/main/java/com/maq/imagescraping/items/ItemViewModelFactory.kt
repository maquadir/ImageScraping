package com.maq.propertyapp.properties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//viewmodel factory class to create view models

@Suppress("UNCHECKED_CAST")
class ItemViewModelFactory : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItemViewModel() as T
    }

}