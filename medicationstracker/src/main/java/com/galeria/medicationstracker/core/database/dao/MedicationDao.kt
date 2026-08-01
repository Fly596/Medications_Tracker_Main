package com.galeria.medicationstracker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galeria.medicationstracker.core.database.entity.DayOfWeek
import com.galeria.medicationstracker.core.database.entity.MedicationDayEntity
import com.galeria.medicationstracker.core.database.entity.MedicationEntity
import com.galeria.medicationstracker.core.database.entity.MedicationWithDays
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertMedication(medication: MedicationEntity)

  @Insert
  suspend fun insertDays(days: List<MedicationDayEntity>)

  /**
   * ТРАНЗАКЦИЯ: Выполняет запись в обе таблицы как единую неразделимую операцию.
   * Если запись дней упадет с ошибкой, запись самого лекарства отменится (Rollback).
   */
  @Transaction
  suspend fun insertMedicationWithDays(
    medication: MedicationEntity,
    days: List<MedicationDayEntity>
  ) {
    insertMedication(medication)
    insertDays(days)
  }

  /**
   * Запрос 1: Получить ВСЕ лекарства со всеми днями приема.
   * Аннотация @Transaction обязательна для @Relation, чтобы Room сделал 2 запроса атомарно.
   */
  @Transaction
  @Query("SELECT * FROM medications")
  fun getAllMedicationsWithDays(): Flow<List<MedicationWithDays>>

  /**
   * Запрос 2: Найти только те лекарства, которые нужно пить в конкретный день (например, в ПОНЕДЕЛЬНИК).
   * Благодаря индексу на `dayOfWeek` запрос выполняется мгновенно.
   */
  @Transaction
  @Query(
    """
        SELECT * FROM medications 
        WHERE id IN (
            SELECT medicationId FROM medication_days WHERE dayOfWeek = :day
        )
    """
  )
  fun getMedicationsForDay(day: DayOfWeek): Flow<List<MedicationWithDays>>

  /**
   * Запрос 3: Посчитать количество лекарств, назначенных на конкретный день.
   */
  @Query("SELECT COUNT(DISTINCT medicationId) FROM medication_days WHERE dayOfWeek = :day")
  fun getMedicationCountForDay(day: DayOfWeek): Flow<Int>

  /**
   * Запрос 4: Получить конкретное лекарство по его ID.
   * Аннотация @Transaction обязательна для @Relation, чтобы Room сделал 2 запроса атомарно.
   */
  @Transaction
  @Query("SELECT * FROM medications WHERE id = :medicationId")
  fun getMedicationWithDays(medicationId: String): Flow<MedicationWithDays>
}