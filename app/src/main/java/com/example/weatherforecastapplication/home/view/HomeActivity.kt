package com.example.weatherforecastapplication.home.view

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.ActivityHomeBinding
import com.example.weatherforecastapplication.dialog.customdialog.CustomDialog
import com.example.weatherforecastapplication.home.viewmodel.HomeActivityViewModel
import com.example.weatherforecastapplication.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HomeActivity : AppCompatActivity(), DialogInterface.OnDismissListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences


    // view Model
    private val viewModel: HomeActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // init shared pref
        sharedPreferences = initSharedPref(this)

        if (isFirst()) {
            // check with shared
            // here init first shared for all
            initUNIT(Units.METRIC.name, this)
            when (Locale.getDefault().language.toString()) {
                "en" -> {
                    initLan("en", this)
                }
                "ar" -> {
                    initLan("ar", this)

                }
            }

            binding.container.visibility = View.INVISIBLE
            CustomDialog().show(supportFragmentManager, "MyCustomFragment")
        }


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    private fun isFirst(): Boolean {
        return initSharedPref(this).getBoolean(getString(R.string.FIRST), true)
    }


    override fun onDismiss(p0: DialogInterface?) {

        finish()
        startActivity(intent)
    }

}