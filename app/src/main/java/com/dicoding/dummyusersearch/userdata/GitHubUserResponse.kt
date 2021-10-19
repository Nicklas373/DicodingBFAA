package com.dicoding.dummyusersearch.userdata

import com.google.gson.annotations.SerializedName

data class GitHubUserResponse(

    @field:SerializedName("items")
    val items: ArrayList<GitHubUserArray>,
)