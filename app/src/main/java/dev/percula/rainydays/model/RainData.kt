package dev.percula.rainydays.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import dev.percula.rainydays.db.LocalDateSerializer
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDate

@Serializable
@Entity(primaryKeys = [RainData.Keys.STATION, RainData.Keys.DATE])
data class RainData(@ColumnInfo(name = Keys.DATE) @Serializable(with=LocalDateSerializer::class) @SerializedName(Keys.DATE) val date: LocalDate,
                    @ColumnInfo(name = Keys.STATION) @SerializedName(Keys.STATION) val station: String,
                    @ColumnInfo(name = Keys.PRCP) @SerializedName(Keys.PRCP) val precipitation: Double) {

    object Keys {
        const val DATE = "DATE"
        const val STATION = "STATION"
        const val PRCP = "PRCP"
    }

}