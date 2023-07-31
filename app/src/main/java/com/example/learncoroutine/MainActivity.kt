package com.example.learncoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.learncoroutine.databinding.ActivityMainBinding

/**
 * @author Jacky
 * @date 2023/7/31
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val countViewModel: CountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStop.text = "暂停"
        initListener()
    }

    private fun initListener() {
        countViewModel.isCountingDown.observe(this) {
            binding.btnStop.text = if (it) "暂停" else "恢复"
        }

        countViewModel.countdownTime.observe(this) {
            binding.tvCount.text = countViewModel.formatTime(it)
        }

        binding.btnStart.setOnClickListener {
            countViewModel.startCountdown(binding.etInput.text.toString().toDoubleOrNull() ?: 0.0)
        }

        binding.btnStop.setOnClickListener {
            if (countViewModel.isCountingDown.value == true) {
                countViewModel.pauseCountdown()
            } else {
                countViewModel.resumeCountdown()
            }
        }
    }
}