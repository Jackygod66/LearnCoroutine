package com.example.learncoroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.text.DecimalFormat

/**
 * ViewModel
 * 用于倒计时的ViewModel，精确到小数点后两位
 */
class CountViewModel: ViewModel() {

    private val _countdownTime = MutableLiveData<Double>()

    val countdownTime: LiveData<Double>
        get() = _countdownTime

    private val _isCountingDown = MutableLiveData<Boolean>()

    val isCountingDown: LiveData<Boolean>
        get() = _isCountingDown

    private var job: Job? = null

    private var remainingTime = 0.0
    private val decimalFormat = DecimalFormat("0.00")

    fun formatTime(timeInSeconds: Double): String {
        return decimalFormat.format(timeInSeconds)
    }

    fun startCountdown(timeInSeconds: Double) {
        if (job?.isActive == true) {
            job?.cancel() // 如果有正在运行的倒计时，先取消它
        }

        job = viewModelScope.launch {
            remainingTime = formatTime(timeInSeconds).toDouble()
            while (remainingTime > 0) {
                delay(10) // 等待10毫秒
                remainingTime -= 0.01 // 每次减去0.01秒
                remainingTime = formatTime(remainingTime).toDouble()
                _countdownTime.value = remainingTime
            }
        }
        _isCountingDown.value = true
    }

    fun pauseCountdown() {
        job?.cancel()
        _isCountingDown.value = false
    }

    fun resumeCountdown() {
        startCountdown(remainingTime)
        _isCountingDown.value = true
    }

    override fun onCleared() {
        super.onCleared()
        pauseCountdown()
    }

}