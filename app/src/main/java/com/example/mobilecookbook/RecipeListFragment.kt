package com.example.mobilecookbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class RecipeListFragment : Fragment() {
    private lateinit var recipeListView: ListView
    private lateinit var addButton: Button
    private lateinit var recipes: ArrayList<Recipe>
    private lateinit var adapter: ArrayAdapter<Recipe>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        recipeListView = view.findViewById(R.id.recipe_list)
        addButton = view.findViewById(R.id.add_recipe_button)

        recipes = loadRecipes()

        adapter = object : ArrayAdapter<Recipe>(
            requireContext(),
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            recipes
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val recipe = getItem(position)
                view.findViewById<TextView>(android.R.id.text1).text = recipe?.name
                view.findViewById<TextView>(android.R.id.text2).text = "Rating: ${recipe?.rating}/5.0"
                return view
            }
        }

        recipeListView.adapter = adapter

        recipeListView.setOnItemClickListener { _, _, position, _ ->
            val recipe = recipes[position]
            val detailsFragment = RecipeDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("name", recipe.name)
                    putString("description", recipe.description)
                    putFloat("rating", recipe.rating)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit()
        }

        addButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddRecipeFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        recipes.clear()
        recipes.addAll(loadRecipes())
        adapter.notifyDataSetChanged()
    }

    private fun loadRecipes(): ArrayList<Recipe> {
        val recipeList = ArrayList<Recipe>()
        val sharedPreferences = requireActivity().getSharedPreferences("mojeDane", Context.MODE_PRIVATE)

        val recipeCount = sharedPreferences.getInt("recipe_count", 0)

        for (i in 0 until recipeCount) {
            val name = sharedPreferences.getString("recipe_${i}_name", null)
            if (name != null) {
                val description = sharedPreferences.getString("recipe_${i}_description", "")
                val rating = sharedPreferences.getFloat("recipe_${i}_rating", 0f)
                recipeList.add(Recipe(name, description ?: "", rating))
            }
        }

        return recipeList
    }

    companion object {
        fun saveRecipes(context: Context, recipes: List<Recipe>) {
            val sharedPreferences = context.getSharedPreferences("mojeDane", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("recipe_count", recipes.size)

            recipes.forEachIndexed { index, recipe ->
                editor.putString("recipe_${index}_name", recipe.name)
                editor.putString("recipe_${index}_description", recipe.description)
                editor.putFloat("recipe_${index}_rating", recipe.rating)
            }

            editor.apply()
        }

        fun updateRecipeRating(context: Context, recipeName: String, newRating: Float) {
            val recipesList = loadRecipesStatic(context)

            for (i in recipesList.indices) {
                if (recipesList[i].name == recipeName) {
                    recipesList[i] = Recipe(
                        recipesList[i].name,
                        recipesList[i].description,
                        newRating
                    )
                    break
                }
            }

            saveRecipes(context, recipesList)
        }

        private fun loadRecipesStatic(context: Context): ArrayList<Recipe> {
            val recipeList = ArrayList<Recipe>()
            val sharedPreferences = context.getSharedPreferences("mojeDane", Context.MODE_PRIVATE)

            val recipeCount = sharedPreferences.getInt("recipe_count", 0)

            for (i in 0 until recipeCount) {
                val name = sharedPreferences.getString("recipe_${i}_name", null)
                if (name != null) {
                    val description = sharedPreferences.getString("recipe_${i}_description", "")
                    val rating = sharedPreferences.getFloat("recipe_${i}_rating", 0f)
                    recipeList.add(Recipe(name, description ?: "", rating))
                }
            }

            return recipeList
        }
    }
}