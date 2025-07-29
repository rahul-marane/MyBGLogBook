package com.rahul.minilogbook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BGLogDao {
    @Insert
    suspend fun insert(bgLogEntry: BGLogEntry)

    @Query("SELECT * FROM bg_log_entries ORDER BY timeStamp DESC")
    fun getAllEntries(): Flow<List<BGLogEntry>>

    @Query("DELETE FROM bg_log_entries")
    suspend fun deleteAll()


}