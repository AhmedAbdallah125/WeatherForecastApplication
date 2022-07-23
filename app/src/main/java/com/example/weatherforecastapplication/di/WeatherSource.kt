package com.example.weatherforecastapplication.di

import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.local.LocalSourceInterface
import com.example.weatherforecastapplication.datasource.network.ConcreteRemote
import com.example.weatherforecastapplication.datasource.network.RemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class WeatherDataBaseModule {
    @Binds
    abstract fun bindWeatherDataBase(concreteLocalSource: ConcreteLocalSource): LocalSourceInterface
}

@InstallIn(SingletonComponent::class)
@Module
abstract class WeatherRemoteModule {
    @Binds
    abstract fun bindRemoteWeather(concreteRemote: ConcreteRemote): RemoteSource
}