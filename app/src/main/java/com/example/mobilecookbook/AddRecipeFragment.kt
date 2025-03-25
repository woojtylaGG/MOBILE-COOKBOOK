package com.example.mobilecookbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class AddRecipeFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        nameEditText = view.findViewById(R.id.recipe_name_edit)
        descriptionEditText = view.findViewById(R.id.recipe_description_edit)
        saveButton = view.findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            saveRecipe()
        }

        return view
    }

    private fun saveRecipe() {
        val name = nameEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            return
        }

        val recipesList = ArrayList<Recipe>()
        val sharedPreferences = requireActivity().getSharedPreferences("mojeDane", Context.MODE_PRIVATE)
        val recipeCount = sharedPreferences.getInt("recipe_count", 0)

        for (i in 0 until recipeCount) {
            val existingName = sharedPreferences.getString("recipe_${i}_name", null)
            if (existingName != null) {
                val existingDescription = sharedPreferences.getString("recipe_${i}_description", "")
                val existingRating = sharedPreferences.getFloat("recipe_${i}_rating", 0f)
                recipesList.add(Recipe(existingName, existingDescription ?: "", existingRating))
            }
        }

        recipesList.add(Recipe(name, description, 0f))

        RecipeListFragment.saveRecipes(requireContext(), recipesList)

        Toast.makeText(context, "Przepis zapisany!", Toast.LENGTH_SHORT).show()

        requireActivity().supportFragmentManager.popBackStack()
    }
}