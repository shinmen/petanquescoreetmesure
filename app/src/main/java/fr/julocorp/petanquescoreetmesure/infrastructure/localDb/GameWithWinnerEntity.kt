package fr.julocorp.petanquescoreetmesure.infrastructure.localDb

import androidx.room.Embedded
import androidx.room.Relation

data class GameWithWinnerEntity(
    @Embedded val gameEntity: GameEntity,
    @Relation(
        entity = TeamEntity::class,
        parentColumn = "winner_id",
        entityColumn = "id"
    )
    val teamEntity: TeamEntity,
)
