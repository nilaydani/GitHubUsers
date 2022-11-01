package com.nilay.githubusers.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
* Data class that holds the required information for a user item
*/
@Parcelize
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    @SerializedName("id") val id: String,
    @SerializedName("login") val name: String,
    @SerializedName("score") val score: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("url") val url: String,
    @SerializedName("html_url") val htmlUrl: String
) : Parcelable