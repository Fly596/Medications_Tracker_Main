package com.galeria.medtracker2.feature.meds.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galeria.medtracker2.feature.meds.domain.DomainMedication

@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    // В 1 единице (таблетке), для не мед препаратов мб упустить.
    val doseMg: Double?,
    val stock: Double?, // Кол-во штук/грамм..
    val stockMeasureUnit: String?, // Показывает это штуки или граммы.
    val drugClass: String?, // stim/opioid/benz..
) {
    
    fun toDomain(): DomainMedication {
        return DomainMedication(
            id = id,
            name = name,
            doseMg = doseMg,
            stock = stock,
            stockMeasureUnit = stockMeasureUnit,
            drugClass = drugClass,
        )
    }
    
    companion object {
        
        fun fromDomainToEntity(domainMedication: DomainMedication): MedicationEntity {
            return MedicationEntity(
                id = domainMedication.id,
                name = domainMedication.name,
                doseMg = domainMedication.doseMg,
                stock = domainMedication.stock,
                stockMeasureUnit = domainMedication.stockMeasureUnit,
                drugClass = domainMedication.drugClass,
            )
        }
    }
}
