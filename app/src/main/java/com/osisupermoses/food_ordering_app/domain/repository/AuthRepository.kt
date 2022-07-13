package com.osisupermoses.food_ordering_app.domain.repository

import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.model.Card
import com.osisupermoses.food_ordering_app.domain.model.Restaurant
import com.osisupermoses.food_ordering_app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isUserAuthenticatedInFirebase(): Boolean

    suspend fun register(email: String, password: String): Flow<Resource<Boolean>>

    suspend fun login(email: String, password: String): Flow<Resource<Boolean>>

    suspend fun forgotPassword(email: String): Flow<Resource<Boolean>>

    suspend fun loginWithGoogle(token: String): Flow<Resource<Boolean>>

    suspend fun getUserInfoFromFirestore(): Flow<Resource<List<User>>>

    suspend fun getRestaurantInfoFromFirestore(): Flow<Resource<List<Restaurant>>>

    suspend fun getCardInfoFromFirestore(): Flow<Resource<List<Card>>>

    suspend fun deleteUserAccount(): Flow<Resource<Boolean>>

    fun getFirebaseAuthState(): Flow<Boolean>
}