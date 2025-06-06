package com.example.frontend.di

import com.example.frontend.data.dto.GoogleAuthApi
import com.example.frontend.data.dto.PaymentStatus
import com.example.frontend.data.remote.CartApiService
import com.example.frontend.data.remote.LoginApiService
import com.example.frontend.data.remote.OrderApiService
import com.example.frontend.data.remote.PaymentApiService
import com.example.frontend.data.remote.ProductApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
val gson = GsonBuilder().setLenient().create()
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            // Json.asConverterFactory("application/json".toMediaType())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit):  ProductApiService{
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService{
        return retrofit.create(LoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCartApiService(retrofit: Retrofit): CartApiService{
        return retrofit.create(CartApiService::class.java)
    }

    @Provides
    fun provideGoogleAuthApi(retrofit: Retrofit): GoogleAuthApi =
        retrofit.create(GoogleAuthApi::class.java)

    @Provides
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService{
        return retrofit.create(OrderApiService::class.java)
    }
    @Provides
    fun providePaymentApiService(retrofit: Retrofit): PaymentApiService{
        return retrofit.create(PaymentApiService::class.java)
    }
}