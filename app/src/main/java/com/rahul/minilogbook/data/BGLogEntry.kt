package com.rahul.minilogbook.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bg_log_entries")
data class BGLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: Double,
    val unit : BGUnit,
    val timeStamp: Long = System.currentTimeMillis()
)