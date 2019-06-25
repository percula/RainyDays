package dev.percula.rainydays.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Location(@PrimaryKey var id: String,
                    var name: String? = null,
                    var latitude: Double? = null,
                    var longitude: Double? = null): Parcelable {

    @Ignore
    val summary = "$latitude, $longitude"

}