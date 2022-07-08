package com.osisupermoses.food_ordering_app.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.data.repository.AuthRepositoryImpl
import com.osisupermoses.food_ordering_app.data.repository.FoodOrderingRepoImpl
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.domain.repository.FoodOrderingRepository
import com.osisupermoses.food_ordering_app.util.paystack.CheckoutActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FoodAppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(auth)

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = DataStoreRepository(context)

    @Singleton
    @Provides
    fun provideCheckoutActivity(
        @ApplicationContext context: Context
    ) = CheckoutActivity(context)

    @Provides
    @Singleton
    fun provideFoodOrderingRepository(): FoodOrderingRepository = FoodOrderingRepoImpl()
}