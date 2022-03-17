package com.example.weatherforecastapplication.dialog.alertdialog.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.viewModels
import com.example.weatherforecastapplication.dialog.alertdialog.viewmodel.AlertTimeDialogViewModel
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.AlertTimeDialogFragmentBinding
import com.example.weatherforecastapplication.model.convertLongToDay
import com.example.weatherforecastapplication.model.convertToTime
import com.example.weatherforecastapplication.model.getCurrentLan
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertTimeDialog : Fragment() {

    private lateinit var binding: AlertTimeDialogFragmentBinding
    private var timeList = mutableListOf<Long>()
    private var dayList = mutableListOf<Long>()


    private val viewModel: AlertTimeDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AlertTimeDialogFragmentBinding.inflate(layoutInflater, container, false)
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
        // handle save --> check empty put Default

    }


    private fun setInitialTime(current: Long) {
        val timeNow = convertToTime(current, requireContext())
        val oneHour = 1000 * 60 * 60 * 60
        val timeAfter = convertToTime(current + oneHour, requireContext())
        // date
        val dateNow = convertLongToDay(current, requireContext())
        //apply
        binding.btnFrom.text = timeNow.plus("\n").plus(dateNow)
        binding.btnTo.text = timeAfter.plus("\n").plus(dateNow)
    }


    private fun initTimePicker(isFrom: Boolean, dayPicker: Long) {

        val calendar = Calendar.getInstance()
        val currentHour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute: Int = calendar.get(Calendar.MINUTE)


        val listener: (TimePicker?, Int, Int) -> Unit =
            { timePicker: TimePicker?, hour: Int, minute: Int ->
                //save
                val time = TimeUnit.MINUTES.toMillis(minute.toLong()) + TimeUnit.HOURS.toMillis(
                    minute.toLong()
                )
                timeList.add(time)
                val timeString = convertToTime(time, requireContext())
                val dayString = convertLongToDay(dayPicker, requireContext())
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
                    dayList.add(getDateMillis(date))
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
        return d.time
    }

    private fun bindView(timeString: String, dayString: String, isFrom: Boolean) {
        val text = timeString.plus("\n").plus(dayString)
        if (isFrom) {
            binding.btnFrom.text = text

        } else {
            binding.btnTo.text = text
        }
    }

}
