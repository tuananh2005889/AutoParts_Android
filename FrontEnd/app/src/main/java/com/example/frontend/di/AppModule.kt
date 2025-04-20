//package com.example.frontend.di
//
//import android.content.Context
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule{
//   @Provides
//   @Singleton
//   fun provideContext(@ApplicationContext appContext: Context): Context{
//      return appContext
//   }
//
////   @Provides
////   @Singleton
////   fun provideAuthManager(authManager: AuthManager): AuthManager{
////      return authManager
////   }
//}
