package com.example.weatherforecastapplication.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.home.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.model.IRepository

class FavouriteViewModelFactory(private var myRepository: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            return FavouriteViewModel(myRepository) as T
        } else {
            throw IllegalArgumentException("View Model class Not found")
        }
    }
}