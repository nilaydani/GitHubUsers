package com.nilay.githubusers.githubuserslist.api

import com.nilay.githubusers.api.UserResponse
import com.nilay.githubusers.api.UsersApi
import com.nilay.githubusers.model.User
import retrofit2.http.Query
import java.io.IOException

/**
 * [FakeUsersApi] class extends the [UsersApi] class to
 * create a fake api in order to simulate the serach users service
 * */
class FakeUsersApi: UsersApi {

    private val usersList = arrayListOf<User>();
    private var failureMsg: String? = null

    override suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): UserResponse {
        failureMsg?.let {
            throw IOException(it)
        }

        return UserResponse(
            totalCount = usersList.size,
            results = usersList
        )
    }

    /**
    * params -> user: [User] object
    **/
    fun addUser(user: User){
        // add user to list
        usersList.add(user)
    }
}