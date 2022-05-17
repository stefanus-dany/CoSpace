package id.stefanusdany.cospace.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultRecommendationEntity(
    var id: String = "",
    var valueRecommendation: Double = 0.0
) : Parcelable
