package com.galeria.medicationstracker.feature_medications.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RegimentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegiment(regiment: RegimentsEntity)

    @Delete
    suspend fun deleteRegiment(regiment: RegimentsEntity)

    @Update
    suspend fun updateRegiment(regiment: RegimentsEntity)

    @Query("SELECT * FROM regiments")
    fun getAllRegiments(): Flow<List<RegimentsEntity>>

    @Query("SELECT * FROM regiments WHERE id = :id")
    suspend fun getRegimentById(id: Int): RegimentsEntity?

}
