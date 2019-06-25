package dev.percula.rainydays.db

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Serializer(forClass = LocalDate::class)
object LocalDateSerializer: KSerializer<LocalDate> {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE

    override val descriptor: SerialDescriptor = StringDescriptor

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, obj: LocalDate) {
        return encoder.encodeString(obj.format(dateFormatter))
    }
}

@Serializer(forClass = Double::class)
object DoubleSerializer: KSerializer<Double> {

    override val descriptor: SerialDescriptor = StringDescriptor

    override fun deserialize(decoder: Decoder): Double {
        return decoder.decodeString().toDoubleOrNull() ?: 0.0
    }

    override fun serialize(encoder: Encoder, obj: Double) {
        return encoder.encodeString(obj.toString())
    }
}