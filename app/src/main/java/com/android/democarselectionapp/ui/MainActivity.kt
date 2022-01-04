package com.android.democarselectionapp.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.android.democarselectionapp.R
import com.android.democarselectionapp.databinding.ActivityMainBinding
import com.android.democarselectionapp.initializeNetworkLiveData
import com.android.democarselectionapp.listenToNetworkUpdate
import com.android.democarselectionapp.viewmodel.MainViewModel

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        application.initializeNetworkLiveData()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        listenToNetworkUpdate(binding.activityLayout)
        val view = binding.root
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(view)
    }
}