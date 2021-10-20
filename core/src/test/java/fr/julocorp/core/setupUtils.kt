package fr.julocorp.core

import arrow.core.Either
import arrow.core.flatMap
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Team
import fr.julocorp.core.repository.GameRepository
import org.mockito.Mockito
import org.mockito.kotlin.any
import java.util.*


suspend fun <T, R> setUpStartGame(
    id: UUID,
    teams: List<Team>,
    repository: GameRepository,
    test: suspend (Either<Throwable, Game>) -> Either<T, R>
) {
    Game.startGame(id, teams, repository).flatMap { test(it) }.mapLeft { it }
}


suspend fun setUpGameRepository(): GameRepository {
    val gameRepository = Mockito.mock(GameRepository::class.java)
    Mockito.`when`(gameRepository.startGame(any())).thenReturn(Either.Right(Unit))

    return gameRepository
}