package com.example.mobil4lab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MealAdapter(
    private var meals: MutableList<MealItem>,
    private val onItemClicked: (item: MealItem) -> Unit
) : RecyclerView.Adapter<MealViewHolder>() {

    fun updateMeals(newMeals: MutableList<MealItem>) {
        this.meals.clear()
        this.meals.addAll(newMeals)
        notifyDataSetChanged()
    }

    private fun onViewHolderClicked(position: Int) {
        onItemClicked(meals[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.meal_item, parent, false)
        return MealViewHolder(view, this::onViewHolderClicked)
    }

    override fun getItemCount(): Int = meals.size

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }
}

class MealViewHolder(
    itemView: View,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val nameView: TextView = itemView.findViewById(R.id.meal_name)
    private val statusView: TextView = itemView.findViewById(R.id.meal_status)
    private val categoryView: TextView = itemView.findViewById(R.id.meal_category)

    init {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
    }

    fun bind(item: MealItem) {
        nameView.text = item.strMeal
        statusView.text = itemView.context.getString(R.string.Meal_adapter_area, item.strArea ?: itemView.context.getString(
            R.string.meal_adapter_no_area
        ))
        categoryView.text =
            itemView.context.getString(R.string.meal_adaptr_categor, item.strCategory ?: itemView.context.getString(
                R.string.meal_adapter_no_category
            ))
    }
}