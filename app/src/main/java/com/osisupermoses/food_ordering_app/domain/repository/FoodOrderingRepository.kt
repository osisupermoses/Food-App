package com.osisupermoses.food_ordering_app.domain.repository

import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.model.Food

import com.osisupermoses.food_ordering_app.domain.model.RecipesItem
import com.osisupermoses.food_ordering_app.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface FoodOrderingRepository {

    fun getFoodList(): Flow<Resource<List<Food>>>

    fun getRestaurantList(): Flow<Resource<List<Restaurant>>>

    fun getRecipeDetails(id: Int): Resource<RecipesItem>
}