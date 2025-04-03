package com.example.frontend.di

import com.example.frontend.data.remote.ApiService
import com.example.frontend.data.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProductRepository(apiService: ApiService): ProductRepository {
        return ProductRepository(apiService)
    }
}