package com.nilay.githubusers.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nilay.githubusers.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}