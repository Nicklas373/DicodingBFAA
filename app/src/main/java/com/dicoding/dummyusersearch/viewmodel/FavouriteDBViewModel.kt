package com.dicoding.dummyusersearch.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dicoding.dummyusersearch.database.FavouriteDB
import com.dicoding.dummyusersearch.database.FavouriteRoomDB

class FavouriteDBViewModel : ViewModel() {

    private lateinit var favResult: LiveData<List<FavouriteDB>>
    private lateinit var favDB: LiveData<List<FavouriteDB>>

    fun subscribeFavResult(context: Context): LiveData<List<FavouriteDB>> {
        val database = FavouriteRoomDB.getDatabase(context.applicationContext).favouriteDao()
        favResult = database.getAllFavourites().map { data ->
            data.reversed().map { FavouriteDB(it.id, it.login, it.avatarUrl, it.htmlUrl) }
        }

        favDB = favResult
        return favDB
    }
}