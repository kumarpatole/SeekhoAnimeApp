package com.kumar.seekhoanimeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kumar.seekhoanimeapp.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE mal_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT mal_id FROM favorites")
    suspend fun getAllFavoriteIds(): List<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE mal_id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}
