package com.example.simbirsofttodolist.view.adapters

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsofttodolist.R
import com.example.simbirsofttodolist.database.Deal
import com.example.simbirsofttodolist.databinding.DealItemBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class DealAdapter() : RecyclerView.Adapter<DealAdapter.DealHolder>() {
    private val dealList = ArrayList<Deal>()

    class DealHolder(deal: View) : RecyclerView.ViewHolder(deal) {
        private val binding = DealItemBinding.bind(deal)
        fun bind(deal: Deal) = with(binding){
            dealName.text = deal.name
            dealTime.text = getDate(deal.dateFinish)
            dealDesc.text = deal.description
        }

        @SuppressLint("SimpleDateFormat")
        private fun getDate(timestamp: Long): String{
            return try {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestamp * 1000L
                return DateFormat.format("dd-MM-yyyy HH:mm:ss", calendar).toString()
            } catch (e: Exception) {
                "xx"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deal_item, parent, false)
        return DealHolder(view)
    }

    override fun onBindViewHolder(holder: DealHolder, position: Int) {
        holder.bind(dealList[position])
    }

    override fun getItemCount(): Int {
        return dealList.size
    }

    fun removeItem(position: Int){
        if(dealList.isNotEmpty() && dealList.size > position) {
            dealList.removeAt(position)
            notifyDataSetChanged()
        }
        if(dealList.size == 0){
            dealList.clear()
        }
    }

    fun addAllDeals(deal: Deal){
        dealList.add(deal)
        notifyDataSetChanged()
    }
    fun clearList(){
        dealList.clear()
    }
    fun getDealId(position: Int): Int? {
        return dealList[position].id
    }
}