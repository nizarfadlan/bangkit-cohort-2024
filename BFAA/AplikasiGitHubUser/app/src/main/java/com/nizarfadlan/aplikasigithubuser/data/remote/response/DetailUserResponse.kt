package com.nizarfadlan.aplikasigithubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("login")
    val login: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("followers")
    val followers: Int? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("html_url")
    val htmlUrl: String? = null,

    @field:SerializedName("following")
    val following: Int? = null,

    @field:SerializedName("name")
    val name: String? = null
)
