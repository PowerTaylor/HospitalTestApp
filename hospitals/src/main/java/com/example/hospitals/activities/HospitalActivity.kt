package com.example.hospitals.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hospitals.R
import com.example.hospitals.viewmodels.HospitalViewModel
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
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
