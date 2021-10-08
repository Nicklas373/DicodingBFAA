package com.dicoding.dummyusersearch.api

import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import com.dicoding.dummyusersearch.userdata.GitHubUserResponse
import com.dicoding.dummyusersearch.userdata.GitHubUserJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUserQuery(
        @Query("q") username: String
    ): Call<GitHubUserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<GitHubUserJSON>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<GitHubUserArray>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<GitHubUserArray>>
}