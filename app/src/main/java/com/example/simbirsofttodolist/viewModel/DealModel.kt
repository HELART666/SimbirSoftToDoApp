package com.example.simbirsofttodolist.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simbirsofttodolist.database.Deal

open class DealModel: ViewModel() {
    val deal: MutableLiveData<Deal> by lazy {
        MutableLiveData<Deal>()
    }
}