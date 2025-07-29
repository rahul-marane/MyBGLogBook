package com.rahul.minilogbook.di

import android.content.Context
import androidx.room.Room
import com.rahul.minilogbook.data.BGLogDao
import com.rahul.minilogbook.data.BGLogDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
 * Hilt module for providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of the [BGLogDatabase].
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) :BGLogDatabase {
        return Room.databaseBuilder(
            context,
            BGLogDatabase :: class.java,
            "bg_database"
        ).build()
    }

    /**
     * Provides an instance of the [BGLogDao].
     */
    @Provides
    fun provideDao(database: BGLogDatabase) : BGLogDao  = database.bgLogDao()
}