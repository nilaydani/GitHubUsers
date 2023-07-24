package com.nilay.githubusers.githubuserslist;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.nilay.githubusers.api.UserDetailsApi;
import com.nilay.githubusers.api.UsersApi;
import com.nilay.githubusers.room.AppDatabase;

import javax.inject.Singleton;

import dagger.Provides;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.testing.HiltAndroidTest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@HiltAndroidTest
public class TestApplication extends Application {

}
