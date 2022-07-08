package com.osisupermoses.food_ordering_app.data.repository

import coil.network.HttpException
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.model.*
import com.osisupermoses.food_ordering_app.domain.repository.FoodOrderingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FoodOrderingRepoImpl : FoodOrderingRepository {
    override fun getFoodList(): Flow<Resource<List<Food>>> =
        flow {
        //Dummy local data
        try {
            val result = getFoods()
            emit(Resource.Success(data = result))
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(
                message = e.message ?: "Couldn't reach server. Check your internet connection"
            ))
        }
    }

    override fun getRestaurantList(): Flow<Resource<List<Restaurant>>> =
        flow {
        //Dummy local data
       try {
            val result = getRestaurants()
            emit(Resource.Success(data = result))
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(
                message = e.message ?: "Couldn't reach server. Check your internet connection"
            ))
        }
    }

    override fun getRecipeDetails(id: Int): Resource<RecipesItem> {
        //Dummy local data
        return try {
            val result = getRecipeItemList().firstOrNull { it.id == id }
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