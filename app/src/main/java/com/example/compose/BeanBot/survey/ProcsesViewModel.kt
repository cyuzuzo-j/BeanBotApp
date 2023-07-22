package com.example.compose.BeanBot.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.compose.BeanBot.Screen
import com.example.compose.BeanBot.util.Event

class ProcssViewModel : ViewModel() {

    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>> = _navigateTo
    var ipadress = ""
}

class ProcsesviewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCH" +
            "ECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProcssViewModel::class.java)) {
            return ProcssViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}