package com.example.lab4mob

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.lab4mob.databinding.DetailBinding
import kotlinx.coroutines.launch


class Detail : BaseActivity() {
    private lateinit var binding: DetailBinding
    private val viewModel by viewModels<MealViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar("Meal Details", showBackButton = true)

        val mealId = intent.getStringExtra("meal_id")
        if (mealId != null) {
            viewModel.loadMealDetails(mealId)
        }

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state.isLoading) {
                    binding.detailName.text = getString(R.string.Detail_loading_text)
                } else if (state.error != null) {
                    binding.detailName.text = getString(R.string.Detail_fail_text, state.error)
                } else {
                    state.selectedMeal?.let { meal ->
                        binding.detailName.text = meal.strMeal
                        binding.detailStatus.text = getString(R.string.area2, meal.strArea ?: "ХЗ")
                        binding.detailInstructions.text = meal.strInstructions?.replace("\r\n", "\n") ?: "Нет рецепта"
                        meal.strMealThumb?.let { url ->
                            Glide.with(this@Detail)
                                .load(url.replace("\\/", "/"))
                                .into(binding.detailImage)
                        }
                    }
                }
            }
        }
    }
}