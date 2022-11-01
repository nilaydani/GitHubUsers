package com.nilay.githubusers.di

import android.content.Context
import androidx.room.Room
import com.nilay.githubusers.api.UserDetailsApi
import com.nilay.githubusers.api.UsersApi
import com.nilay.githubusers.api.UsersApi.Companion.BASE_URL
import com.nilay.githubusers.room.AppDatabase
import com.nilay.githubusers.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * App module that builds and provides the Retroif Api service
 * @InstallIn annotation enables Hilt to inject the dependency into [SingleComponent],
 * making all the dependencies available in the application activities
 * @Module annotation informs Hilt to provide instances of only certain types
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provies the retrofit Api.
     * Annotating the function with @Provides informs Hilt to provide instances of
     * this type since [Retrofit] class comes from an external library
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    /**
     * Provies users Api
     * */
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UsersApi =
        retrofit.create(UsersApi::class.java)

    /**
     * Provies userDetails Api
     * */
    @Provides
    @Singleton
    fun provideUserDetailsApi(retrofit: Retrofit): UserDetailsApi =
        retrofit.create(UserDetailsApi::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Favorites"
        ).build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}