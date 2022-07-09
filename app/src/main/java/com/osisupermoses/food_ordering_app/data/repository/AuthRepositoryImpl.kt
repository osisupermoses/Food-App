package com.osisupermoses.food_ordering_app.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.model.Card
import com.osisupermoses.food_ordering_app.domain.model.User
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.IOException

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {
    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun register(email: String, password: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            auth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(data = true))

        } catch(e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun login(email: String, password: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            auth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(data = true))

        } catch(e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun forgotPassword(email: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            auth.sendPasswordResetEmail(email).await()
            emit(Resource.Success(data = true))

        } catch(e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun loginWithGoogle(token: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val credential = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(credential).await()
            emit(Resource.Success(data = true))

        } catch(e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getUserInfoFromFirestore(): Flow<Resource<List<User>>> = flow {
        try {
            emit(Resource.Loading())
            val data = FirebaseFirestore.getInstance().collection(Constants.DB_Collection)
                .get().await().documents.map { documentSnapshot ->
                    documentSnapshot.toObject(User::class.java)!!
                }
            emit(Resource.Success(data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCardInfoFromFirestore(): Flow<Resource<List<Card>>>  = flow {
        try {
            emit(Resource.Loading())
            val data = FirebaseFirestore.getInstance().collection(Constants.DB_Collection_Cards)
                .get().await().documents.map { documentSnapshot ->
                    documentSnapshot.toObject(Card::class.java)!!
                }
            emit(Resource.Success(data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logOut(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            auth.currentUser?.apply {
                delete().await()
                emit(Resource.Success(data = true))
            }

        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: e.toString()))
        }
    }

    override fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow  {
        val authStateListener = FirebaseAuth.AuthStateListener { mAuth ->
            trySend(mAuth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.flowOn(Dispatchers.IO)
}