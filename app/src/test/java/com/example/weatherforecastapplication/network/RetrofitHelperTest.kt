package com.example.weatherforecastapplication.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecastapplication.model.OpenWeatherJason
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


class RetrofitHelperTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun add() {
        assertEquals(1, 1)
    }

    @Test
    fun getCurrentWeather_LatAndLong_ReturnOpenWeather() {
        val lat = 31.4175
        val long = 31.814444
        val retrofitHelper = RetrofitHelper
        runBlocking {
            val result = retrofitHelper.getCurrentWeather(lat, long)
            assertEquals(result.body()?.lat,lat)
        }
    }


}
