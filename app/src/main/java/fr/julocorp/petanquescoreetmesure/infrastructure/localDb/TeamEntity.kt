package fr.julocorp.petanquescoreetmesure.infrastructure.localDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(value = ["game_id"]),
    ]
)
data class TeamEntity(
    @PrimaryKey val id: String,
    val name: String,
    val score: Int,
    @ColumnInfo(name = "game_id") val gameId: String,
) {
    companion object {
        const val TABLE_NAME = "team"
    }
}
