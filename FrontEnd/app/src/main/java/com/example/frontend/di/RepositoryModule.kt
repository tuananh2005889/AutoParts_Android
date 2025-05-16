package com.example.frontend.di

import com.example.frontend.data.remote.ApiServiceUser          // <-- thêm
import com.example.frontend.data.remote.LoginApiService
import com.example.frontend.data.remote.PaymentApiService
import com.example.frontend.data.remote.ProductApiService
import com.example.frontend.data.remote.ReviewApiService
import com.example.frontend.data.repository.LoginRepository
import com.example.frontend.data.repository.PaymentRepository
import com.example.frontend.data.repository.ProductRepository
import com.example.frontend.data.repository.ReviewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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

    /* ---------- Review ---------- */
    @Provides @Singleton
    fun provideReviewApiService(retrofit: Retrofit): ReviewApiService =
        retrofit.create(ReviewApiService::class.java)

    @Provides @Singleton
    fun provideReviewRepository(api: ReviewApiService): ReviewRepository =
        ReviewRepository(api)

    @Provides @Singleton
    fun providePaymentRepository(api: PaymentApiService): PaymentRepository{
       return PaymentRepository(api)
    }
}
