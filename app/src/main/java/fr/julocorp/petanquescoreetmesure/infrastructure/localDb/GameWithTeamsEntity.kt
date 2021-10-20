package fr.julocorp.petanquescoreetmesure.infrastructure.localDb

import androidx.room.Embedded
import androidx.room.Relation

data class GameWithTeamsEntity(
    @Embedded val gameEntity: GameEntity,
    @Relation(
        entity = TeamEntity::class,
        parentColumn = "id",
        entityColumn = "game_id"
    )
    val teamsEntity: List<TeamEntity>,
)
