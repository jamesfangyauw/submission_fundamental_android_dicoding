package com.james.submissiononefundamentalandroiddicoding.api

import com.james.submissiononefundamentalandroiddicoding.model.DetailUserResponse
import com.james.submissiononefundamentalandroiddicoding.model.GithubResponse
import com.james.submissiononefundamentalandroiddicoding.model.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String
    ): GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String?): DetailUserResponse

    @GET("users/{username}/followers")
  fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>

}