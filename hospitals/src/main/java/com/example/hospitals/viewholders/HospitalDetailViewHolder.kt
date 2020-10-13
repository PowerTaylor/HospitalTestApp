package com.example.hospitals.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitals.R
import com.example.hospitals.models.HospitalViewItemModel
import kotlinx.android.synthetic.main.hospital_detail_item.view.*

class HospitalDetailViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val imageView: ImageView = view.image_view
    private val titleView: TextView = view.title_view
    private val cityView: TextView = view.city_view

    init {
        imageView.setImageResource(R.drawable.hospital)
    }

    fun setupView(itemModel: HospitalViewItemModel) {
        titleView.text = itemModel.name
        cityView.text = itemModel.city
    }
}

// Cleaner to be in here rather than the adapter.
fun HospitalDetailViewHolder.bindWithItemModel(
    hospitalViewItemModel: HospitalViewItemModel
) = setupView(hospitalViewItemModel)
