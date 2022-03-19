package com.example.weatherforecastapplication.dialog.alertdialog.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.work.*
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.AlertTimeDialogFragmentBinding
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.network.RetrofitHelper
import com.example.weatherforecastapplication.dialog.alertdialog.viewmodel.AlertTimeDialogViewModel
import com.example.weatherforecastapplication.dialog.alertdialog.viewmodel.FactoryAlertTimeDialigViewModel
import com.example.weatherforecastapplication.manager.AlertPeriodicManager
import com.example.weatherforecastapplication.model.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertTimeDialog : DialogFragment() {

    private lateinit var binding: AlertTimeDialogFragmentBinding

    private lateinit var weatherAlert: WeatherAlert


    private val viewModel: AlertTimeDialogViewModel by viewModels {
        FactoryAlertTimeDialigViewModel(
            (Repository(
                ConcreteLocalSource(requireContext()),
                RetrofitHelper
            ))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AlertTimeDialogFragmentBinding.inflate(layoutInflater, container, false)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.round_corner)
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val current = Calendar.getInstance().timeInMillis


        setInitialTime(current)
        binding.btnFrom.setOnClickListener {
            initDayPicker(true)
        }
        binding.btnTo.setOnClickListener {
            initDayPicker(false)
        }
        binding.btnSaveAlert.setOnClickListener {

            viewModel.insertWeatherAlert(weatherAlert)
            viewModel.id.observe(viewLifecycleOwner) {
                //have id now
                // make work manager here
                getPermission()
                callPeriodicWorkManager(it)

            }
            dismiss()
        }

    }

    private fun callPeriodicWorkManager(id: Int) {
        val shared = initSharedPref(requireContext())
        val lat = shared.getFloat(getString(R.string.LAT), 0f)
        val long = shared.getFloat(getString(R.string.LON), 0f)
        val data = Data.Builder()
            .putFloat(getString(R.string.LAT), lat)
            .putFloat(getString(R.string.LON), long)
            .putInt(getString(R.string.ID), id).build()
        //constrains
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        //periodic request
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            AlertPeriodicManager::class.java,
            24, TimeUnit.HOURS
        )
            .setInputData(data)
            .setConstraints(constraints)
            .addTag(id.toString())
            .build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            id.toString(), ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    private fun getPermission() {

    }

    private fun setInitialView(current: Long){
        val current = current.div(1000L)
        val timeNow = convertToTime(current, requireContext())
        val oneHour = (3600L) + current
        val timeAfter = convertToTime((oneHour), requireContext())
        // date
        val dateNow = convertToDate(current, requireContext())
        //apply
        binding.btnFrom.text = timeNow.plus("\n").plus(dateNow)
        binding.btnTo.text = timeAfter.plus("\n").plus(dateNow)
    }
    private fun setInitialTime(current: Long) {
        setInitialView(current)
        // set here object
        val calendar = Calendar.getInstance()
        val time = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val date = "$day/${month + 1}/$year"
        val currentNow =getDateMillis(date)

        weatherAlert = WeatherAlert(
            null, (
                    time.toLong() + minute.toLong()), ((time.plus(1)).toLong() + minute.toLong()
                    ), currentNow, currentNow
        )
    }


    private fun initTimePicker(isFrom: Boolean, dayPicker: Long) {

        val calendar = Calendar.getInstance()
        var currentHour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        if (!isFrom) {
            currentHour = currentHour.plus(1)
        }

        val currentMinute: Int = calendar.get(Calendar.MINUTE)


        val listener: (TimePicker?, Int, Int) -> Unit =
            { timePicker: TimePicker?, hour: Int, minute: Int ->
                //save
                var time = (TimeUnit.MINUTES.toSeconds(minute.toLong()) + TimeUnit.HOURS.toSeconds(
                    hour.toLong()
                )
                        )
                time = time.minus(3600L * 2)
                if (isFrom) {
                    weatherAlert.startTime = time
                } else
                    weatherAlert.endTime = time
                val timeString = convertToTime(time, requireContext())
                val dayString = convertToDate(dayPicker, requireContext())
                bindView(timeString, dayString, isFrom)
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            listener, currentHour, currentMinute, false
        )

        timePickerDialog.setTitle(getString(R.string.choose_time))
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()

    }

    private fun initDayPicker(isFrom: Boolean) {

        val myCalender = Calendar.getInstance()
        val year = myCalender[Calendar.YEAR]
        val month = myCalender[Calendar.MONTH]
        val day = myCalender[Calendar.DAY_OF_MONTH]
        val myDateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                if (view.isShown) {

                    val date = "$day/${month + 1}/$year"
                    if (isFrom) {
                        weatherAlert.startDay = getDateMillis(date)
                    } else
                        weatherAlert.endDay = getDateMillis(date)

                    initTimePicker(isFrom, getDateMillis(date))
                }
            }
        val datePickerDialog = DatePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            myDateListener, year, month, day
        )
        val title = "Choose date"
        datePickerDialog.setTitle(title)
        datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()
    }

    private fun getDateMillis(date: String): Long {
        val f = SimpleDateFormat("dd/MM/yyyy", Locale(getCurrentLan(requireContext())))
        val d: Date = f.parse(date)
        return (d.time).div(1000)
    }

    private fun bindView(timeString: String, dayString: String, isFrom: Boolean) {
        val text = timeString.plus("\n").plus(dayString)
        if (isFrom) {
            binding.btnFrom.text = text

        } else {
            binding.btnTo.text = text
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window!!.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}
