package com.osisupermoses.food_ordering_app.domain.repository

import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.model.Food

import com.osisupermoses.food_ordering_app.domain.model.RecipesItem
import com.osisupermoses.food_ordering_app.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface FoodOrderingRepository {

    fun getFoodList(): Resource<List<Food>>

    fun getRestaurantList(): Resource<List<Restaurant>>

    fun getRecipeDetails(id: Int): Resource<RecipesItem>
}