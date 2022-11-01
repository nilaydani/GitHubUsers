package com.nilay.githubusers.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
* Data class that holds the required information for a user item
*/
@Parcelize
data class User(
    @SerializedName("id") val id: String,
    @SerializedName("login") val name: String,
    @SerializedName("score") val score: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("url") val url: String,
    @SerializedName("html_url") val htmlUrl: String
) : Parcelable