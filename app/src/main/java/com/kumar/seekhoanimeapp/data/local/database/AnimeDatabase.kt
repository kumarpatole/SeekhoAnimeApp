package com.kumar.seekhoanimeapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kumar.seekhoanimeapp.data.local.converters.Converters
import com.kumar.seekhoanimeapp.data.local.dao.AnimeDao
import com.kumar.seekhoanimeapp.data.local.dao.FavoriteDao
import com.kumar.seekhoanimeapp.data.local.dao.RemoteKeysDao
import com.kumar.seekhoanimeapp.data.local.entity.AnimeEntity
import com.kumar.seekhoanimeapp.data.local.entity.FavoriteEntity
import com.kumar.seekhoanimeapp.data.local.entity.RemoteKeysEntity

@Database(entities = [AnimeEntity::class, RemoteKeysEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun favoriteDao(): FavoriteDao

}