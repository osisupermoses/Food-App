package com.osisupermoses.food_ordering_app.data.repository

import coil.network.HttpException
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.model.*
import com.osisupermoses.food_ordering_app.domain.repository.FoodOrderingRepository
import java.io.IOException

class FoodOrderingRepoImpl : FoodOrderingRepository {

    override fun getFoodList(): Resource<List<Food>> {
        //Dummy local data
        return try {
            val result = getFoods()
            Resource.Success(data = result)
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = e.message ?: "Couldn't reach server. Check your internet connection"
            )
        }
    }

    override fun getRestaurantList(): Resource<List<Restaurant>> {
        //Dummy local data
        return try {
            val result = getRestaurants()
            Resource.Success(data = result)
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = e.message ?: "Couldn't reach server. Check your internet connection"
            )
        }
    }

    override fun getRecipeDetails(id: Long): Resource<RecipesItem> {
        //Dummy local data
        return try {
            val result = getRecipeItemList().firstOrNull { it.foodId == id }
            Resource.Success(data = result)
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred")
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = e.message ?: "Couldn't reach server. Check your internet connection"
            )
        }
    }
}