package com.example.mobilecookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class RecipeDetailsFragment : Fragment() {

    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var ratingBar: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_details, container, false)

        nameTextView = view.findViewById(R.id.recipe_name)
        descriptionTextView = view.findViewById(R.id.recipe_description)
        ratingBar = view.findViewById(R.id.rating_bar)

        val name = arguments?.getString("name", "")
        val description = arguments?.getString("description", "")
        val rating = arguments?.getFloat("rating", 0f)

        nameTextView.text = name
        descriptionTextView.text = description
        if (rating != null) {
            ratingBar.rating = rating
        }

        ratingBar.setOnRatingBarChangeListener { _, newRating, _ ->
            updateRating(name ?: "", newRating)
        }

        return view
    }

    private fun updateRating(recipeName: String, newRating: Float) {
        RecipeListFragment.updateRecipeRating(requireContext(), recipeName, newRating)
        Toast.makeText(context, "Ocena zaktualizowana!", Toast.LENGTH_SHORT).show()
    }
}