package com.nizarfadlan.aplikasigithubuser.data.remote.retrofit

import com.nizarfadlan.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.nizarfadlan.aplikasigithubuser.data.remote.response.ItemsItem
import com.nizarfadlan.aplikasigithubuser.data.remote.response.SearchUserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    suspend fun searchUser(
        @Query("q", encoded = true) query: String
    ): SearchUserResponse

    @GET("/users/{username}")
    suspend fun detailUser(
        @Path("username") id: String
    ): DetailUserResponse

    @GET("/users/{username}/followers")
    suspend fun followersUser(
        @Path("username") id: String
    ): List<ItemsItem>

    @GET("/users/{username}/following")
    suspend fun followingUser(
        @Path("username") id: String,
    ): List<ItemsItem>
}