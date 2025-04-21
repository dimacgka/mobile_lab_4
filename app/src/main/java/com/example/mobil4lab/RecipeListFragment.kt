package com.example.mobil4lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeListFragment : Fragment() {
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var adapter: MealAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = MealAdapter(mutableListOf()) { meal ->
            (activity as? MainActivity)?.setCurrentRecipeId(meal.idMeal)
            val action = RecipeListFragmentDirections.actionToRecipeDetail(meal.idMeal)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter

        viewModel.recipes.observe(viewLifecycleOwner) { meals ->
            adapter.updateMeals(meals.toMutableList())
        }

        viewModel.loadRecipes()
    }
}