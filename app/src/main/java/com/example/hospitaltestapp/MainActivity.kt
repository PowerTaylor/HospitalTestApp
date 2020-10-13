package com.example.hospitaltestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hospitals.fragments.HospitalListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        attachHospitalFragmentIfRequired()
    }

    private fun attachHospitalFragmentIfRequired() {
        val existingFragment = supportFragmentManager.findFragmentByTag(HospitalListFragment.TAG)

        if (existingFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    HospitalListFragment.newInstance(),
                    HospitalListFragment.TAG
                )
                .commit()
        }
    }
}
