package com.example.codogen.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anzid.annotation.PublicLiveData

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    @PublicLiveData private val _test = MutableLiveData<String>()
}