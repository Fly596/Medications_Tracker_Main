package com.galeria.medtracker2.navigation

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

@Serializable
sealed interface AppRoutes {

    @Serializable data object Home : AppRoutes // расписание приемов на сегодня.

    @Serializable data object AddMedication : AppRoutes

    @Serializable data object MedicationsList : AppRoutes // Мои приемы

    @Serializable
    data class Medication(@Serializable(with = UUIDSerializer::class) val id: UUID) : AppRoutes
}

object UUIDSerializer : KSerializer<UUID> {

    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}
