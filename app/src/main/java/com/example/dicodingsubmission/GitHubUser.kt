package com.example.dicodingsubmission

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubUser(
    val gitImage: String?,
    val gitName: String?,
    val gitId: String?,
    val gitFollower: String?,
    val gitFollowing: String?,
    val gitComp: String?,
    val gitLocation: String?,
    val gitRepo: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    companion object : Parceler<GitHubUser> {

        override fun GitHubUser.write(parcel: Parcel, flags: Int) {
            parcel.writeString(gitImage)
            parcel.writeString(gitName)
            parcel.writeString(gitId)
            parcel.writeString(gitFollower)
            parcel.writeString(gitFollowing)
            parcel.writeString(gitComp)
            parcel.writeString(gitLocation)
            parcel.writeString(gitRepo)
        }

        override fun create(parcel: Parcel): GitHubUser {
            return GitHubUser(parcel)
        }
    }
}