package com.dicoding.dummyusersearch.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavouriteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: FavouriteDB)

    @Update
    fun update(fav: FavouriteDB)

    @Query("DELETE from FavouriteDB where login =:login")
    fun delete(login: String)

    @Query("SELECT * from FavouriteDB ORDER BY id ASC")
    fun getAllFavourites(): LiveData<List<FavouriteDB>>

    @Query("SELECT EXISTS (SELECT * from FavouriteDB where login =:login)")
    fun checkUserFavourites(login: String): Boolean
}