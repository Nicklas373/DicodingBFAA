package com.dicoding.dummyusersearch.userdata

import com.google.gson.annotations.SerializedName

data class GitHubUserResponse(

    @field:SerializedName("total_count")
    val totalCount: String,

    @field:SerializedName("incomplete_results")
    val results: String,

    @field:SerializedName("items")
    val items: ArrayList<GitHubUserArray>,
)