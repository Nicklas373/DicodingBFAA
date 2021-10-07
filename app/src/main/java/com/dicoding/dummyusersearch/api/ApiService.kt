package com.dicoding.dummyusersearch.api

import com.dicoding.dummyusersearch.userdata.GitHubUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: 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")
    fun getUserQuery(
        @Query("q") username: String
    ): Call<GitHubUserResponse>

}