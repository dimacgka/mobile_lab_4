package com.example.mobil4lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecipeDetailFragment : Fragment() {
    private val viewModel: RecipeViewModel by viewModels()
    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailName: TextView = view.findViewById(R.id.detailName)
        val detailStatus: TextView = view.findViewById(R.id.detailStatus)
        val detailInstructions: TextView = view.findViewById(R.id.detailInstructions)
        val detailImage: ImageView = view.findViewById(R.id.detailImage)
        val toStepsButton: Button = view.findViewById(R.id.to_steps)
        val addToShoppingListButton: Button = view.findViewById(R.id.add_to_shopping_list)

        toStepsButton.setOnClickListener {
            val action = RecipeDetailFragmentDirections.actionToCookingSteps(args.recipeId)
            findNavController().navigate(action)
        }

        addToShoppingListButton.setOnClickListener {
            val meal = viewModel.recipeDetails.value
            val ingredients = meal?.getIngredientsWithMeasures() ?: emptyList()

            val message = if (ingredients.isEmpty()) {
                getString(R.string.detail_dialog_fr_no_ingr)
            } else {
                ingredients.joinToString("\n") { "${it.first} - ${it.second}" }
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_reciept_det_fr))
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_detail_fr_yes)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.detail_dialog_fr_no)) { dialog, _ -> dialog.dismiss() }
                .show()
        }

        viewModel.recipeDetails.observe(viewLifecycleOwner) { meal ->
            meal?.let {
                detailName.text = it.strMeal
                detailStatus.text = getString(
                    R.string.area_detail_fr,
                    it.strArea ?: getString(R.string.detail_dialog_fr_unknown)
                )
                detailInstructions.text = it.strInstructions?.replace("\r\n", "\n")
                    ?: getString(R.string.detail_dialog_fr_no_instructions)
                it.strMealThumb?.let { url ->
                    Glide.with(this@RecipeDetailFragment)
                        .load(url.replace("\\/", "/"))
                        .into(detailImage)
                }
            } ?: run {
                detailName.text = getString(R.string.error_loading_details_Recipe_fr)
            }
        }

        viewModel.loadRecipeDetails(args.recipeId)
    }
}