package com.example.hospitaltestapp

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.hospitals.fragments.HospitalListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var hospitalListFragment: HospitalListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        attachHospitalFragmentIfRequired()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu?.findItem(R.id.action_filter)?.apply {
            setOnMenuItemClickListener { onFilterItemClicked() }
        }

        return true
    }

    private fun onFilterItemClicked(): Boolean {
        hospitalListFragment.onFilterClicked()
        return true
    }

    private fun attachHospitalFragmentIfRequired() {
        val existingFragment = supportFragmentManager.findFragmentByTag(HospitalListFragment.TAG)

        if (existingFragment == null) {
            hospitalListFragment = HospitalListFragment.newInstance()

            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    hospitalListFragment,
                    HospitalListFragment.TAG
                )
                .commit()
        } else {
            hospitalListFragment = existingFragment as HospitalListFragment
        }
    }
}
