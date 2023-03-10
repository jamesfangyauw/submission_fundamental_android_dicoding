package com.james.submissiononefundamentalandroiddicoding.api

import com.james.submissiononefundamentalandroiddicoding.model.DetailUserResponse
import com.james.submissiononefundamentalandroiddicoding.model.GithubResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService{
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun searchUser (
        @Query("q") query : String
    ) : Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username : String) : Call<DetailUserResponse>

}