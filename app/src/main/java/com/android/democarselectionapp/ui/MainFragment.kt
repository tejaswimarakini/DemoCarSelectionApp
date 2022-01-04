package com.android.democarselectionapp.ui

import CustomLoadStateAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.democarselectionapp.R
import com.android.democarselectionapp.databinding.MainFragmentBinding
import com.android.democarselectionapp.paging.MainListAdapter
import com.android.democarselectionapp.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment(), MainListAdapter.OnClickListener {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = MainFragmentBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainListAdapter = MainListAdapter()
        mainListAdapter.setOnClickListener(this)
        val layoutManagerObj = LinearLayoutManager(activity)
        binding.manufacturerRecyclerView.apply {
            layoutManager = layoutManagerObj
            addItemDecoration(
                DividerItemDecoration(
                    activity?.baseContext,
                    layoutManagerObj.orientation
                )
            )
            adapter = mainListAdapter
            adapter = mainListAdapter.withLoadStateFooter(footer = CustomLoadStateAdapter {
                mainListAdapter.retry()
            })
        }

        lifecycleScope.launch {
            viewModel.getManuFacturesData().collectLatest { pagedData ->
                binding.manufacturerRecyclerView.visibility = View.VISIBLE
                mainListAdapter.submitData(lifecycle, pagedData)
            }
        }
    }

    override fun onItemClicked(carId: String, carName: String) {
        viewModel.setManufacturerLiveData(carId)
        viewModel.setSelectedCarNameLiveData(carName)
        findNavController().navigate(R.id.action_mainFragment_to_carModelFragment)
    }
}