package com.android.democarselectionapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.democarselectionapp.*
import com.android.democarselectionapp.databinding.CarDetailviewBinding
import com.android.democarselectionapp.databinding.CarModelLayoutBinding
import com.android.democarselectionapp.viewmodel.MainViewModel

class CarDetailFragment : Fragment() {

    private lateinit var binding: CarDetailviewBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var builtYearList = mutableListOf("Select Year")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CarDetailviewBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getBuiltYearLiveData().observe(viewLifecycleOwner) { wkdResponse ->
            wkdResponse?.wkda?.let {
                for (obj in wkdResponse.wkda) {
                    builtYearList.add(obj.value)
                }
            }
        }

        activity?.let {
            getInterConnectivity(it) { isNetworkAvailable ->
                when {
                    isNetworkAvailable -> {
                        viewModel.getBuiltDates()
                    }
                }
            }
        }

        val spinner = binding.spinner
        activity?.baseContext?.let { context ->
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                builtYearList
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = it
            }
        }
        spinner.visibility = View.VISIBLE
        spinner.getItem { spinnerValue ->
            val strSpinValue = if (spinnerValue.contains("Select")) "Built Year" else spinnerValue
            viewModel.setSelectedBuiltYearLiveData(strSpinValue)
        }
    }

    override fun onStop() {
        super.onStop()
        builtYearList.clear()
        viewModel.getBuiltYearLiveData().value = null
    }
}