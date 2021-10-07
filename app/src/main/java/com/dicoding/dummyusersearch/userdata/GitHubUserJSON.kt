package com.dicoding.dummyusersearch.userdata

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubUserJSON(

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

) : Parcelable