package com.dicoding.dummyusersearch.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dicoding.dummyusersearch.database.FavouriteDB
import com.dicoding.dummyusersearch.database.FavouriteRoomDB

class FavouriteDBViewModel : ViewModel() {

    private lateinit var githubUserFavouriteList: LiveData<List<FavouriteDB>>
    private lateinit var githubUserFavouriteResult: LiveData<List<FavouriteDB>>

    fun getGitHubUserFavouriteData(context: Context): LiveData<List<FavouriteDB>> {
        val database = FavouriteRoomDB.getDatabase(context.applicationContext).favouriteDao()

        githubUserFavouriteList = database.getAllFavourites().map { data ->
            data.reversed().map { FavouriteDB(it.id, it.login, it.htmlUrl, it.avatarUrl) }
        }

        githubUserFavouriteResult = githubUserFavouriteList
        return githubUserFavouriteResult
    }
}