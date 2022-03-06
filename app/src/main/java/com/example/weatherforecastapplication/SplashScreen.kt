package com.example.weatherforecastapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecastapplication.databinding.ScreenSplashBinding
import kotlinx.coroutines.*

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ScreenSplashBinding
    private val splashScreenScope =lifecycleScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScreenSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // will be go to main Screen
        splashScreenScope.launch(Dispatchers.Default) {
            delay(3000)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        splashScreenScope.cancel()
    }
}