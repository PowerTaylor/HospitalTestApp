package com.example.hospitals

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class HospitalListFragment : Fragment() {

    companion object {
        const val TAG = "HospitalListFragment"

        fun newInstance() = HospitalListFragment()
    }

    private lateinit var viewModel: HospitalListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hospital_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HospitalListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}