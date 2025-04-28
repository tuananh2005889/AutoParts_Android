package com.example.frontend.di

import com.example.frontend.data.remote.ApiServiceUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiServiceUser(retrofit: Retrofit): ApiServiceUser {
        return retrofit.create(ApiServiceUser::class.java)
    }
}
