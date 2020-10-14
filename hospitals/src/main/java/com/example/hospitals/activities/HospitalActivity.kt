package com.example.hospitals.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hospitals.R
import com.example.hospitals.models.HospitalItemModel
import com.example.hospitals.viewmodels.HospitalViewModel
import kotlinx.android.synthetic.main.activity_hospital.*
import org.koin.android.viewmodel.ext.android.viewModel

class HospitalActivity : AppCompatActivity() {

    companion object {
        private const val HOSPITAL_ID = "hospitalId"

        fun startActivity(context: Context, hospitalId: Long) = with(context) {
            val intent = Intent(this, HospitalActivity::class.java)
                .apply {
                    putExtra(HOSPITAL_ID, hospitalId)
                }

            startActivity(intent)
        }
    }

    private val viewModel: HospitalViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.elevation = 0f

        intent?.let {
            val hospitalId = it.getLongExtra(HOSPITAL_ID, -1)
            viewModel.initialise(hospitalId = hospitalId)
        }

        observeViewState()
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this, {
            if (!it.showError && it.hospitalItemModel != null) {
                updateViewWithHospital(it.hospitalItemModel)
            }
        })
    }

    private fun updateViewWithHospital(model: HospitalItemModel) {
        handleTitle(model.name)
        handleAddress(model)
        handleSector(model.sector)
        handleWebsite(model.website)
        handlePhoneNumber(model.number)
    }

    private fun handleTitle(name: String) {
        title_view.text = String.format(
            "%s%s",
            resources.getString(R.string.name_prefix),
            name
        )
    }

    private fun handleAddress(model: HospitalItemModel) {
        val address = StringBuilder()
            .apply {
                if (model.addressLine1.isNotBlank()) append("${model.addressLine1}\n")
                if (model.addressLine2.isNotBlank()) append("${model.addressLine2}\n")
                if (model.addressLine3.isNotBlank()) append("${model.addressLine3}\n")
                if (model.city.isNotBlank()) append("${model.city}\n")
                if (model.postCode.isNotBlank()) append(model.postCode)
            }

        address_view.text = String.format(
            "%s%s",
            resources.getString(R.string.address_prefix),
            address.toString().trim()
        )
    }

    private fun handleSector(sector: String) {
        if (sector.isNotBlank()) {
            sector_view.visibility = View.VISIBLE
            sector_view.text = String.format(
                "%s%s",
                resources.getString(R.string.sector_prefix),
                sector
            )
        } else {
            sector_view.visibility = View.GONE
        }
    }

    private fun handleWebsite(website: String) {
        if (website.isNotBlank()) {
            website_view.visibility = View.VISIBLE

            website_view.text = String.format(
                "%s%s",
                resources.getString(
                    R.string.website_prefix
                ),
                website
            )
        } else {
            website_view.visibility = View.GONE
        }
    }

    private fun handlePhoneNumber(number: String) {
        if (number.isNotBlank()) {
            call_button.setOnClickListener { openPhoneWithNumber(number) }
            call_button.visibility = View.VISIBLE
        } else {
            call_button.visibility = View.GONE
        }
    }

    private fun openPhoneWithNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
