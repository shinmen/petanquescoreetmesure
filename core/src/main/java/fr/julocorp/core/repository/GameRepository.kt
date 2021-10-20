package fr.julocorp.core.repository

import arrow.core.Either
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Team
import fr.julocorp.core.model.Winner
import java.util.*

interface GameRepository {
    suspend fun winRound(team: Team): Either<Throwable, Unit>

    suspend fun startGame(game: Game): Either<Throwable, Unit>

    suspend fun endGame(game: Game, winner: Winner): Either<Throwable, Unit>

    suspend fun findTeamById(id: UUID): Either<Throwable, Team?>
}