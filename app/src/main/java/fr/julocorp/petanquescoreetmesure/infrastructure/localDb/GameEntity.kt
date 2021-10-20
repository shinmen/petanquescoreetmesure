package fr.julocorp.petanquescoreetmesure.infrastructure.localDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "winner_id") val winnerId: String? = null,
) {
    companion object {
        const val TABLE_NAME = "game"
    }
}