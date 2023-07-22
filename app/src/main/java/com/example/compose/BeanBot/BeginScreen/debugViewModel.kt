package com.example.compose.BeanBot.BeginScreen


import androidx.lifecycle.*
import com.example.compose.BeanBot.Screen
import com.example.compose.BeanBot.util.Arduino_Communicate.servo_angles
import com.example.compose.BeanBot.util.Event

class DebugViewModel : ViewModel() {
    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>> = _navigateTo
    var arduino_servos_angles = servo_angles
}

class DebugviewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCH" +
            "ECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DebugViewModel::class.java)) {
            return DebugViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}