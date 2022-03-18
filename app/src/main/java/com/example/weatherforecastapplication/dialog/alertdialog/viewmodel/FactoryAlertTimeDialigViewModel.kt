package com.example.weatherforecastapplication.dialog.alertdialog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.alert.view.viewmodel.AlertViewModel
import com.example.weatherforecastapplication.model.IRepository

class FactoryAlertTimeDialigViewModel (private var myRepository: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertTimeDialogViewModel::class.java)) {
            return AlertTimeDialogViewModel(myRepository) as T
        } else {
            throw IllegalArgumentException("View Model class Not found")
        }
    }
}