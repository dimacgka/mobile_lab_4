package com.example.mobil4lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CookingStepsFragment : Fragment() {
    private val viewModel: RecipeViewModel by viewModels()
    private val args: CookingStepsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cooking_steps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stepsTextView: TextView = view.findViewById(R.id.stepsTextView)
        val addToShoppingListButton: Button = view.findViewById(R.id.add_to_shopping_list)

        viewModel.cookingSteps.observe(viewLifecycleOwner) { steps ->
            stepsTextView.text = steps.joinToString("\n")
        }

        addToShoppingListButton.setOnClickListener {
            viewModel.recipeDetails.value?.let { meal ->
                val ingredientsList = meal.getIngredientsWithMeasures()
                val ingredientsText = ingredientsList.joinToString("\n") { (ingredient, measure) ->
                    "$measure $ingredient"
                }


                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.dialog_reciept_det_fr))
                    .setMessage(ingredientsText)
                    .setPositiveButton(getString(R.string.dialog_detail_fr_yes)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNegativeButton(getString(R.string.detail_dialog_fr_no)) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
        viewModel.loadCookingSteps(args.recipeId)
        viewModel.loadRecipeDetails(args.recipeId)
    }


}