package com.rahul.minilogbook.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for accessing Blood Glucose Log data.
 * This class provides an abstraction layer over the data sources.
 *
 * @property dao The Data Access Object for BGLogEntry.
 */
@Singleton
class BGLogRepository @Inject constructor(private val dao: BGLogDao) {

    /**
     * Retrieves all blood glucose log entries as a [Flow].
     */
    fun getAllEntries() = dao.getAllEntries()

    /**
     * Inserts a new blood glucose log entry into the database.
     */
    suspend fun insert(bgLogEntry: BGLogEntry) = dao.insert(bgLogEntry)


    /**
     * Deletes all blood glucose log entries from the database.
     */
    suspend fun deleteAll() = dao.deleteAll()
}