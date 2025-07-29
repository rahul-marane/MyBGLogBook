package com.rahul.minilogbook.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BGLogEntry::class], version = 1)
abstract class BGLogDatabase : RoomDatabase() {
    abstract fun bgLogDao() : BGLogDao
}