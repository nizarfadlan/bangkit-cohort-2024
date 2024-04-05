package com.nizarfadlan.submissionakhiraplikasiakhir.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    var title: String,
    var description: String,
    var thumbnail: String,
    var timeLearn: Int,
    var tags: Array<String>,
    var stars: Float
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Course

        if (title != other.title) return false
        if (description != other.description) return false
        if (thumbnail != other.thumbnail) return false
        if (timeLearn != other.timeLearn) return false
        if (!tags.contentEquals(other.tags)) return false
        return stars == other.stars
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + thumbnail.hashCode()
        result = 31 * result + timeLearn
        result = 31 * result + tags.contentHashCode()
        result = 31 * result + stars.hashCode()
        return result
    }
}