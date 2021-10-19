package com.dicoding.dummyusersearch.api

import com.dicoding.dummyusersearch.BuildConfig
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import com.dicoding.dummyusersearch.userdata.GitHubUserJSON
import com.dicoding.dummyusersearch.userdata.GitHubUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token $apiKEY")
    fun getUserQuery(
        @Query("q") username: String
    ): Call<GitHubUserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $apiKEY")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<GitHubUserJSON>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $apiKEY")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<GitHubUserArray>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $apiKEY")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<GitHubUserArray>>

    companion object {
        const val apiKEY: String = BuildConfig.KEY
    }
}