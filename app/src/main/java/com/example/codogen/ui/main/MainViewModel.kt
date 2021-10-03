package com.example.codogen.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anzid.annotation.PublicLiveData
import com.anzid.annotation.PublicSharedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    @PublicLiveData private val _test = MutableLiveData<String>()
    @PublicSharedFlow private val _mutableEvent = MutableSharedFlow<Unit>()
    @PublicSharedFlow(nullable = true) private val _mutableEventNull = MutableSharedFlow<Unit?>()
    @PublicSharedFlow() private val _mutableNull = MutableSharedFlow<List<Unit?>>()

    init {
        viewModelScope.launch {
            delay(1000)
            _mutableEvent.emit(Unit)
            delay(1000)
            _test.postValue("Hello livedata")
        }
    }
}