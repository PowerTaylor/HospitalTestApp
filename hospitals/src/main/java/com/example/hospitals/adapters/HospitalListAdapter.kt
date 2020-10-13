package com.example.hospitals.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hospitals.R
import com.example.hospitals.models.HospitalViewItemModel
import com.example.hospitals.viewholders.HospitalDetailViewHolder
import com.example.hospitals.viewholders.bindWithItemModel

class HospitalListAdapter : RecyclerView.Adapter<ViewHolder>() {

    var items = listOf<HospitalViewItemModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.hospital_detail_item, parent, false)
        return HospitalDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is HospitalDetailViewHolder -> holder.bindWithItemModel(items[position])
        }
    }

    override fun getItemCount(): Int = items.size
}
