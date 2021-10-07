package com.dicoding.dummyusersearch.api

import com.dicoding.dummyusersearch.userdata.GitHubUserResponse
import com.dicoding.dummyusersearch.userdata.GitHubUserJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token YOUR_GITHUB_TOKEN")
    fun getUserQuery(
        @Query("q") username: String
    ): Call<GitHubUserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token YOUR_GITHUB_TOKEN")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<GitHubUserJSON>
}