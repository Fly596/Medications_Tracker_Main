package com.galeria.medicationstracker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MedicationDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMedication(medication: MedicationEntity)

  /**
   * Запрос 1: Получить ВСЕ лекарства со всеми днями приема.
   */
  @Query("SELECT * FROM medications")
  fun getAllMedicationsWithDays(): Flow<List<MedicationEntity>>

  /**
   * Запрос 2: Получить конкретное лекарство по его ID.
   */
  @Query("SELECT * FROM medications WHERE id = :medicationId")
  fun getMedicationWithDays(medicationId: String): Flow<MedicationEntity>

  /**
   * Запрос 3: Получить лекарства, которые нужно принимать сегодня.
   */
  @Query(
    """
        SELECT * FROM medications 
        WHERE :date BETWEEN startDate AND endDate 
          AND daysOfWeek LIKE '%' || :dayOfWeek || '%'
    """
  )
  fun getMedicationsForDate(
    date: LocalDate,
    dayOfWeek: String // Сюда передаем dayOfWeek.name (например, "MONDAY")
  ): Flow<List<MedicationEntity>>
}