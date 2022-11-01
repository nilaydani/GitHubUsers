package com.nilay.githubusers.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nilay.githubusers.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

    @Insert
    fun insertUser(user: User)

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteUser(id: String)

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    fun getUserById(id: String): User

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id)")
    fun isRowIsExist(id: String): Boolean
}