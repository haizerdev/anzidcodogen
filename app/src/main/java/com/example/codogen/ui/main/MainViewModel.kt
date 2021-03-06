package com.example.codogen.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anzid.annotation.PublicLiveData
import com.anzid.annotation.PublicSharedFlow
import com.anzid.annotation.PublicStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    @PublicLiveData private val _test = MutableLiveData<String>()
    @PublicSharedFlow private val _mutableEvent = MutableSharedFlow<Unit>()
    @PublicSharedFlow(isInternal = true) private val _mutableEventNull = MutableSharedFlow<Unit?>()
    @PublicSharedFlow(true) private val _mutableEventInternal = MutableSharedFlow<Unit?>()
    @PublicLiveData() private val _mutableEventInternal2 = MutableLiveData<String>()
    @PublicStateFlow() private val _mutableNull = MutableStateFlow<List<Unit?>>(emptyList())

    init {
        viewModelScope.launch {
            delay(1000)
            _mutableEvent.emit(Unit)
            delay(1000)
            _test.postValue("Hello livedata")
        }
    }
}