package com.galeria.medtracker2.feature.meds.data.source.remote

import com.galeria.medtracker2.feature.meds.domain.DomainMedication

data class MedicationDto(
    val id: String,
    val name: String,
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
        
        fun fromDomainToDto(domainMedication: DomainMedication): MedicationDto {
            return MedicationDto(
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