package com.example.weatherforecastapplication.manager

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.AlertWindowManagerBinding

class AlertWindowManager(
    private val context: Context,
    private val icon: Int,
    private val description: String
) {
    var binding: AlertWindowManagerBinding? = null
    var customDialog: View? = null

    // check connection first
    fun setWindowManager() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customDialog = inflater.inflate(R.layout.alert_window_manager, null)
        binding = AlertWindowManagerBinding.bind(customDialog!!)
        bindViews()
        val LAYOUT_FLAG: Int = getLayoutFlag()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams: WindowManager.LayoutParams = getWindowParams(LAYOUT_FLAG)
        windowManager.addView(customDialog, layoutParams)
    }

    private fun getLayoutFlag(): Int {
        val LAYOUT_FLAG: Int
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        return LAYOUT_FLAG
    }

    private fun getWindowParams(LAYOUT_FLAG: Int): WindowManager.LayoutParams {
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
//        dialog!!.window!!.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        return WindowManager.LayoutParams(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun bindViews() {
        // must contain email
        binding?.alertImg?.setImageResource(icon)
        binding?.alertDes?.text = description
        binding?.alertBtn?.setOnClickListener {
            stopMyService()
            close()
        }
    }

    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(
                customDialog
            )
            customDialog!!.invalidate()
            (customDialog!!.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    private fun stopMyService() {
        context.stopService(Intent(context, AlertService::class.java))
    }
}