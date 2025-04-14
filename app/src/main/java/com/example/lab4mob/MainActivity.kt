package com.example.lab4mob

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.lab4mob.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MealViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar("Meal List")

        val adapter = MealAdapter(mutableListOf()) { item ->
            val intent = Intent(this, Detail::class.java).apply {
                putExtra("meal_id", item.idMeal) // Передаём только ID
            }
            startActivity(intent)
        }
        binding.list.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.isLoading) {
                        binding.result.text = getString(R.string.LoadingInMain)
                    } else if (state.error != null) {
                        binding.result.text = getString(R.string.MainActivError_main, state.error)
                    } else {
                        adapter.updateMeals(state.meals.toMutableList())
                        binding.result.text = ""
                    }
                }
            }
        }
    }
}

