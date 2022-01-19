package fr.julocorp.core

import arrow.core.Either
import arrow.core.Nel
import arrow.core.computations.either
import arrow.core.flatMap
import fr.julocorp.core.exception.GameError
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Team
import fr.julocorp.core.repository.GameRepository
import org.mockito.Mockito
import org.mockito.kotlin.any
import java.util.*


suspend fun setUpStartGame(
    id: UUID,
    teams: List<Team>,
    repository: GameRepository,
): Either<Nel<GameError>, Either<Throwable, Game>> = Game.startGame(id, teams, repository)

suspend fun setUpGameRepository(): GameRepository {
    val gameRepository = Mockito.mock(GameRepository::class.java)
    Mockito.`when`(gameRepository.startGame(any())).thenReturn(Either.Right(Unit))

    return gameRepository
}