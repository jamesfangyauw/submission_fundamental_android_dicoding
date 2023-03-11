package com.james.submissiononefundamentalandroiddicoding.api

import android.os.Build
import com.james.submissiononefundamentalandroiddicoding.BuildConfig
import com.james.submissiononefundamentalandroiddicoding.model.DetailUserResponse
import com.james.submissiononefundamentalandroiddicoding.model.GithubResponse
import com.james.submissiononefundamentalandroiddicoding.model.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService{

    @GET("search/users")

    fun searchUser (
        @Query("q") query : String
    ) : Call<GithubResponse>

    @GET("users/{username}")

    fun getDetailUser(@Path("username") username : String) : Call<DetailUserResponse>

    @GET("users/{username}/followers")

    fun getFollowers (@Path("username") username: String) : Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing (@Path("username") username: String) : Call<List<ItemsItem>>

}