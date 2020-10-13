package com.example.hospitals.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitals.R
import com.example.hospitals.adapters.HospitalListAdapter
import com.example.hospitals.models.HospitalViewItemModel
import com.example.hospitals.viewmodels.HospitalListViewModel

class HospitalListFragment : Fragment() {

    companion object {
        const val TAG = "HospitalListFragment"

        fun newInstance() = HospitalListFragment()
    }

    private lateinit var viewModel: HospitalListViewModel

    private val recyclerView: RecyclerView? by lazy { view?.findViewById(R.id.recycler_view) }
    private val adapter = HospitalListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hospital_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)

        // TODO - Remove. Just for testing.
        updateAdapter(
            listOf(
                HospitalViewItemModel(
                    id = 123,
                    name = "Manchester Hospital",
                    firstLineAddress = "Swinemoor Lane",
                    city = "Manchester",
                    postCode = "HU17 0FA"
                ),
                HospitalViewItemModel(
                    id = 123,
                    name = "Manchester Hospital2",
                    firstLineAddress = "Swinemoor Lane",
                    city = "Manchester",
                    postCode = "HU17 0FA"
                )
            )
        )
    }

    private fun updateAdapter(items: List<HospitalViewItemModel>) {
        adapter.items = items
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HospitalListViewModel::class.java)
        // TODO: Use the ViewModel
    }
}