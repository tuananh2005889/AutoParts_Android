package com.example.frontend.di

import com.example.frontend.data.remote.LoginApiService
import com.example.frontend.data.remote.ProductApiService
import com.example.frontend.data.repository.LoginRepository
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
    fun provideProductRepository(productApiService: ProductApiService): ProductRepository {
        return ProductRepository(productApiService)
    }

    @Provides
    @Singleton
    fun productLoginRepository(loginApiService: LoginApiService): LoginRepository {
        return LoginRepository(loginApiService)
    }

}