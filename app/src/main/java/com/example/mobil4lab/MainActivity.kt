package com.example.mobil4lab

import android.os.Bundle

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.mobil4lab.BaseActivity
import com.example.mobil4lab.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var currentRecipeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_content) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.recipeListFragment -> {
                    navController.popBackStack(R.id.recipeListFragment, false)
                    true
                }
                R.id.recipeDetailFragment -> {
                    if (currentRecipeId != null) {
                        val action = MainGraphDirections.actionGlobalToRecipeDetail(currentRecipeId!!)
                        navController.navigate(action)
                        true
                    } else false
                }
                R.id.cookingStepsFragment -> {
                    if (currentRecipeId != null) {
                        val action = MainGraphDirections.actionGlobalToCookingSteps(currentRecipeId!!)
                        navController.navigate(action)
                        true
                    } else false
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.recipeDetailFragment, R.id.cookingStepsFragment -> {
                    currentRecipeId = arguments?.getString("recipeId")
                }
                R.id.recipeListFragment -> {
                    currentRecipeId = null
                }
            }
        }
    }

    fun setCurrentRecipeId(recipeId: String) {
        currentRecipeId = recipeId
    }
}