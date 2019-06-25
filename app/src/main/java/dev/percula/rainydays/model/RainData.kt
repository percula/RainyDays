package dev.percula.rainydays.model

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import dev.percula.rainydays.R
import dev.percula.rainydays.db.DoubleSerializer
import dev.percula.rainydays.db.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@Serializable
@Entity(primaryKeys = [RainData.Keys.STATION, RainData.Keys.DATE])
data class RainData(
    @ColumnInfo(name = Keys.DATE)
    @Serializable(with = LocalDateSerializer::class)
    @SerialName(Keys.DATE)
    val date: LocalDate,

    @ColumnInfo(name = Keys.STATION)
    @SerialName(Keys.STATION)
    val station: String,

    @ColumnInfo(name = Keys.PRCP)
    @Serializable(with = DoubleSerializer::class)
    @SerialName(Keys.PRCP)
    val precipitation: Double? = null
) {

    object Keys {
        const val DATE = "DATE"
        const val STATION = "STATION"
        const val PRCP = "PRCP"
    }

    fun formattedDate(): String = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

    fun formattedPrecipitation(context: Context? = null): String {
        return if (PreferenceManager.getDefaultSharedPreferences(context)?.getBoolean(context?.getString(R.string.units_key), false) == true) {
            "${precipitation ?: 0.0 * 2.54} cm"
        } else {
            "$precipitation inches"
        }
    }

}