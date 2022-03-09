package com.example.weatherforecastapplication.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.databinding.CardLayoutDayWeatherBinding
import com.example.weatherforecastapplication.databinding.CardLayoutGridItemBinding
import com.example.weatherforecastapplication.model.Condition

class ConditionAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<ConditionAdapter.ViewHolder>() {
    private var conditionList = emptyList<Condition>()
    fun setConditions(conditions: List<Condition>) {
        this.conditionList = conditions
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CardLayoutGridItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imgGrid.setImageResource(conditionList[position].img)
        holder.binding.txtGridDes.text = conditionList[position].des
        holder.binding.txtGridName.text = conditionList[position].name
    }

    override fun getItemCount(): Int {
        return conditionList.size
    }

    inner class ViewHolder(
        val binding: CardLayoutGridItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
}