package com.dicoding.dummyusersearch.userdata

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubUserResponse (

    @field:SerializedName("total_count")
    val total_count: Int,

    @field:SerializedName("incomplete_results")
    val results: String,

    @field:SerializedName("items")
    val items: ArrayList<GitHubUserArray>,
) : Parcelable