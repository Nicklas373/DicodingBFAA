package com.dicoding.dummyusersearch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavouriteDB::class], version = 1)
abstract class FavouriteRoomDB : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDAO

    companion object {
        @Volatile
        private var INSTANCE: FavouriteRoomDB? = null

        @JvmStatic
        fun getDatabase(context: Context): FavouriteRoomDB {
            if (INSTANCE == null) {
                synchronized(FavouriteRoomDB::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavouriteRoomDB::class.java, "favourite_database"
                    )
                        .allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as FavouriteRoomDB
        }
    }
}