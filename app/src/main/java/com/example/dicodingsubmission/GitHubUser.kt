package com.example.dicodingsubmission

import android.os.Parcel
import android.os.Parcelable

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gitImage)
        parcel.writeString(gitName)
        parcel.writeString(gitId)
        parcel.writeString(gitFollower)
        parcel.writeString(gitFollowing)
        parcel.writeString(gitComp)
        parcel.writeString(gitLocation)
        parcel.writeString(gitRepo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GitHubUser> {
        override fun createFromParcel(parcel: Parcel): GitHubUser {
            return GitHubUser(parcel)
        }

        override fun newArray(size: Int): Array<GitHubUser?> {
            return arrayOfNulls(size)
        }
    }
}
