package com.example.hospitals.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.hospitaldata.repositories.HospitalFilterOptions
import com.example.hospitals.R
import com.example.hospitals.activities.HospitalActivity
import com.example.hospitals.adapters.HospitalListAdapter
import com.example.hospitals.models.HospitalViewItemModel
import com.example.hospitals.viewmodels.HospitalListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.viewmodel.ext.android.getViewModel

class HospitalListFragment : Fragment(), HospitalListAdapter.OnClickListener {

    companion object {
        const val TAG = "HospitalListFragment"

        fun newInstance() = HospitalListFragment()
    }

    private lateinit var viewModel: HospitalListViewModel

    private val recyclerView: RecyclerView? by lazy { view?.findViewById(R.id.recycler_view) }
    private val adapter = HospitalListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hospital_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        observeViewModel()
        setupRecyclerView()
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, {
            showHospitals(it.listOfHospitals)
            if (it.showFilter) showFilter(it.currentFilterOptionIndex)
        })
    }

    private fun showHospitals(listOfHospitals: List<HospitalViewItemModel>) =
        updateAdapter(items = listOfHospitals)

    private fun setupRecyclerView() {
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun updateAdapter(items: List<HospitalViewItemModel>) {
        adapter.items = items
    }

    fun onFilterClicked() =
        viewModel.onFilterClicked()

    private fun showFilter(currentFilterOptionIndex: Int) {
        val items = mutableListOf<String>()

        for (filterOption in HospitalFilterOptions.values()) {
            when(filterOption) {
                HospitalFilterOptions.DEFAULT ->
                    items.add(resources.getString(R.string.default_filter))
                HospitalFilterOptions.NHS ->
                    items.add(resources.getString(R.string.nhs_filter))
                HospitalFilterOptions.HAS_WEBSITE ->
                    items.add(resources.getString(R.string.website_filter))
            }
        }

        var checkedItem = currentFilterOptionIndex

        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(resources.getString(R.string.filter_title))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                viewModel.onFilterConfirmed(index = checkedItem)
            }
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setSingleChoiceItems(items.toTypedArray(), checkedItem) { dialog, which ->
                checkedItem = which
            }
            .show()
    }

    override fun onClick(hospitalId: Long) {
        HospitalActivity.startActivity(requireContext(), hospitalId)
    }
}
