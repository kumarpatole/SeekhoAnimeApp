package com.kumar.seekhoanimeapp.di

import android.content.Context
import androidx.room.Room
import com.kumar.seekhoanimeapp.data.local.dao.AnimeDao
import com.kumar.seekhoanimeapp.data.local.database.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AnimeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AnimeDatabase::class.java,
            "seekho_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAnimeDao(database: AnimeDatabase): AnimeDao{
        return database.animeDao()
    }
}