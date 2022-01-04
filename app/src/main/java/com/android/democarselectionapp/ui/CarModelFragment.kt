package com.android.democarselectionapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.democarselectionapp.R
import com.android.democarselectionapp.afterTextChanged
import com.android.democarselectionapp.databinding.CarModelLayoutBinding
import com.android.democarselectionapp.paging.CarModelListAdapter
import com.android.democarselectionapp.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CarModelFragment : Fragment(), CarModelListAdapter.OnClickListener {

    private lateinit var binding: CarModelLayoutBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CarModelLayoutBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = this.viewModel
        val carModelAdapter = CarModelListAdapter()
        carModelAdapter.setOnClickListener(this)
        val layoutManagerObj = LinearLayoutManager(activity)
        binding.carModelRecyclerView.apply {
            layoutManager = layoutManagerObj
            addItemDecoration(
                DividerItemDecoration(
                    activity?.baseContext,
                    layoutManagerObj.orientation
                )
            )
            adapter = carModelAdapter
        }

        lifecycleScope.launch {
            viewModel.getCarModelsData().collectLatest { pagedData ->
                carModelAdapter.submitData(pagedData)
            }
        }

        binding.searchEditTextView.afterTextChanged { queryStr ->
            if (queryStr.length >= 2) {
                dismissSoftKeyboard()
                updateSearchResults(carModelAdapter, queryStr)
            }
        }

        binding.textInputLayout.setEndIconOnClickListener {
            binding.searchEditTextView.text?.clear()
            dismissSoftKeyboard()
            updateSearchResults(carModelAdapter, "")
        }
    }

    private fun dismissSoftKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            binding.searchEditTextView.windowToken,
            0
        )
    }

    private fun updateSearchResults(carModelAdapter: CarModelListAdapter, query: String) {
        viewModel.setSearchQueryLiveData(query)
        lifecycleScope.launch {
            viewModel.getFilteredCarModelsData().collectLatest { pagedData ->
                carModelAdapter.submitData(pagedData)
            }
        }
    }

    override fun onItemClicked(manufacturer: String, model: String) {
        viewModel.setMainTypeCarLiveData(model)
        findNavController().navigate(R.id.action_carModelFragment_to_carDetailFragment)
    }
}
