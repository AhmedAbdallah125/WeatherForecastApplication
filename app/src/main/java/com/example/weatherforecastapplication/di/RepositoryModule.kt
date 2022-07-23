package com.example.weatherforecastapplication.di

import com.example.weatherforecastapplication.model.IRepository
import com.example.weatherforecastapplication.model.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repository: Repository): IRepository
}