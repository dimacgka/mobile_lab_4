package com.example.lab4mob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class MealAdapter(
    private var meals: MutableList<MealItem>,
    private val onItemClicked: (item: MealItem) -> Unit,
): RecyclerView.Adapter<MealViewHolder>() {

    fun updateMeals(newMeals: MutableList<MealItem>) {
        this.meals.clear()
        this.meals.addAll(newMeals)
        notifyDataSetChanged()
    }

    private fun onViewHolderClicked(position: Int) {
        onItemClicked(meals[position])
    }
    /**
     * Метод по созданию ViewHolder
     */
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.meal_item, parent, false)
        return MealViewHolder(view, this::onViewHolderClicked)
    }
    /**
     * Кол-во элементов в адаптере (списке)
     */
    override fun getItemCount(): Int {
        return meals.size
    }
    /**
     * Добавление данных на соответсвующий ViewHolder.
     */
    override fun onBindViewHolder(
        holder: MealViewHolder, position: Int
    ) {
        holder.bind(meals[position])
    }
}
/**
 * ViewHolder элемента списка.
 * Наследуемся от RecyclerView.ViewHolder
 */
class MealViewHolder(
    itemView: View,
    private val onItemClicked: (position: Int) -> Unit
): RecyclerView.ViewHolder(itemView) {
    private val nameView: TextView =
        itemView.findViewById(R.id.meal_name)
    private val statusView: TextView =
        itemView.findViewById(R.id.meal_status)
    private val categoryView: TextView =
        itemView.findViewById(R.id.meal_category)

    init {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
    }
    /**
     * Метод для связывания данных с ViewHolder
     */
    fun bind(item: MealItem) {
        nameView.text = item.strMeal
        statusView.text = itemView.context.getString(R.string.area2, item.strArea ?: "ХЗ")
        categoryView.text =
            itemView.context.getString(R.string.category_in_mealAdapter,
                item.strCategory ?: "ХЗ")
    }
}