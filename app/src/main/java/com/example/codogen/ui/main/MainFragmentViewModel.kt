package com.example.codogen.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anzid.annotation.PublicLiveData
import com.anzid.annotation.PublicStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainFragmentViewModel : ViewModel() {

    @PublicLiveData private val _test = MutableLiveData<String>()
    @PublicLiveData private val _mutableEvent = MutableSharedFlow<Unit>()
    @PublicStateFlow private val _mutableEventNull = MutableStateFlow<Unit?>(null)
    @PublicStateFlow(true) private val _mutableEventInternal = MutableStateFlow<Unit?>(null)
    @PublicLiveData private val _mutableEventInternal2 = MutableLiveData<String>()
    @PublicStateFlow private val _mutableNull = MutableStateFlow<List<Unit?>>(emptyList())

    init {
        viewModelScope.launch {
            delay(1000)
            _mutableEvent.emit(Unit)
            _mutableNull.asSharedFlow()
            delay(1000)
            _test.postValue("Hello livedata")
        }
    }
}