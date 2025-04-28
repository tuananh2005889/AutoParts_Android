package com.example.frontend.di

import com.example.frontend.data.remote.ApiServiceUser          // <-- thêm
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

    /* ---------- Product ---------- */
    @Provides @Singleton
    fun provideProductRepository(
        productApiService: ProductApiService
    ): ProductRepository =
        ProductRepository(productApiService)

    /* ---------- Login ---------- */
    @Provides @Singleton
    fun provideLoginRepository(
        loginApiService: LoginApiService,
        userApi: ApiServiceUser
    ): LoginRepository =
        LoginRepository(loginApiService, userApi)
}
