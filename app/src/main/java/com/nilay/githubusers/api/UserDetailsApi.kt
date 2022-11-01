package com.nilay.githubusers.api

import com.nilay.githubusers.api.UserResponse
import com.nilay.githubusers.model.UserDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Retrofit api setup
 */
interface UserDetailsApi {

    /**
     * returns [UserDetails]
     */
    @Headers("Accept: application/vnd.github.v3+json")
    @GET
    fun getUserDetail(
        @Url query: String
    ): Call<UserDetails>
}