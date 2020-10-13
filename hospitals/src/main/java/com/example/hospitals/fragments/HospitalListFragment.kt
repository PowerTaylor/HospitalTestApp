package com.example.hospitals.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitals.R
import com.example.hospitals.adapters.HospitalListAdapter
import com.example.hospitals.models.HospitalViewItemModel
import com.example.hospitals.viewmodels.HospitalListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.viewmodel.ext.android.getViewModel

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
        viewModel = getViewModel()
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
                    city = "Manchester"
                ),
                HospitalViewItemModel(
                    id = 123,
                    name = "Manchester Hospital2",
                    city = "Manchester"
                )
            )
        )
    }

    private fun updateAdapter(items: List<HospitalViewItemModel>) {
        adapter.items = items
    }

    fun onFilterClicked() {
        val singleItems = arrayOf("Item 1", "Item 2", "Item 3")
        val checkedItem = 1

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.filter_title))
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
                viewModel.onFilterClicked(index = checkedItem)
            }
            .setNeutralButton(resources.getString(R.string.cancel), { dialog, i ->
                // Implement this.
            })
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                // Respond to item chosen
            }
            .show()
    }
}
