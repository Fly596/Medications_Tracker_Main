package com.galeria.medicationstracker.core.database.dao

import androidx.room.Delete
import androidx.room.Update
import androidx.room.Upsert

/**
 * Base DAO interface defining generic CRUD operations for Room entities.
 *
 * @param T The entity type handled by this DAO.
 */
interface BaseDao<T> {

    /**
     * Inserts or updates a single entity.
     * @return The row ID of the inserted or updated entity.
     */
    @Upsert
    suspend fun upsert(entity: T): Long

    /**
     * Inserts or updates a list of entities atomically.
     */
    @Upsert
    suspend fun upsertAll(entities: List<T>)

    /**
     * Deletes a single entity from the database.
     */
    @Delete
    suspend fun delete(entity: T)

    /**
     * Updates an existing entity in the database.
     */
    @Update
    suspend fun update(entity: T)
}