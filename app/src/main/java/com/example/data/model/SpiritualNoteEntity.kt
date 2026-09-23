package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NoteCategory(val code: String, val englishTitle: String, val arabicTitle: String) {
    CONFESSION_PREP("CONFESSION", "Confession Prep & Review", "مراجعة النفس وسر الاعتراف"),
    PERSONAL_PRAYER("PRAYER", "Personal Prayer Petitions", "طلبات وشفاعات شخصية"),
    PATRISTIC_MEDITATION("MEDITATION", "Patristic Reflection", "تأملات روحية وآبائية"),
    THANKSGIVING("THANKSGIVING", "Thanksgiving & Praises", "شكر وتسبيح ومراحم الرب");

    companion object {
        fun fromCode(code: String): NoteCategory {
            return values().firstOrNull { it.code == code } ?: PERSONAL_PRAYER
        }
    }
}

@Entity(tableName = "spiritual_notes")
data class SpiritualNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val categoryCode: String,
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val isCompletedOrConfessed: Boolean = false
)
