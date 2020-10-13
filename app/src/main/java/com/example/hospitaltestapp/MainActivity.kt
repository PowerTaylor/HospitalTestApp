package com.example.hospitaltestapp

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.hospitals.fragments.HospitalListFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

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
        val singleItems = arrayOf("Item 1", "Item 2", "Item 3")
        val checkedItem = 1

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.filter_title))
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
            }
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                // Respond to item chosen
            }
            .show()

        return true
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
