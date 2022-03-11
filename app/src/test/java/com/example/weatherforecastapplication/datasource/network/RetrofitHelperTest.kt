package com.example.weatherforecastapplication.datasource.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.model.Repository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test


class RetrofitHelperTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun add() {
        assertEquals(1, 1)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCurrentWeather_LatAndLong_ReturnOpenWeather() {
        val lat = 31.4175
        val long = 31.814444
    }


}
