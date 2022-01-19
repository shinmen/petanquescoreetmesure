package fr.julocorp.core

import arrow.core.Either
import arrow.core.Nel
import arrow.core.computations.either
import com.google.common.truth.Truth.assertThat
import fr.julocorp.core.exception.*
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Team
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import java.util.*

class StartGameTest {
    @Test
    fun `different valid teams should start the game`() = runBlockingTest {
        val gameRepository = setUpGameRepository()

        either<Any, Unit> {
            val resultGame = Game.startGame(
                UUID.randomUUID(),
                listOf(
                    Team(UUID.randomUUID(), "team", 0),
                    Team(UUID.randomUUID(), "another team", 0)
                ),
                gameRepository
            ).bind()
            val startedGame = resultGame.bind()

            assertThat(startedGame).isInstanceOf(Game::class.java)
        }
    }

    @Test
    fun `valid teams with failed repo save should tail to start the game`() = runBlockingTest {
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.startGame(any())).thenReturn(Either.Left(Throwable()))

        either<Any, Unit> {
            val resultGame = Game.startGame(
                UUID.randomUUID(),
                listOf(
                    Team(UUID.randomUUID(), "team", 0),
                    Team(UUID.randomUUID(), "another team", 0)
                ),
                gameRepository
            ).bind()
            resultGame.mapLeft { error ->
                assertThat(error).isInstanceOf(Throwable::class.java)
            }
        }
    }

    @Test
    fun `multiple invalid game setup should return list of errors`() = runBlockingTest {
        val gameRepository = setUpGameRepository()

        either<Nel<GameError>, Unit> {
            Game.startGame(
                UUID.randomUUID(),
                listOf(Team(UUID.randomUUID(), "team", 5)),
                gameRepository
            ).bind()
        }.mapLeft { error ->
            assertThat(error[0]).isInstanceOf(CannotStartGame::class.java)
            val wrapper = error[0] as CannotStartGame
            assertThat(wrapper.reasons.size).isEqualTo(2)
            assertThat(wrapper.reasons[0]).isInstanceOf(NotEnoughTeamToStartGame::class.java)
            assertThat(wrapper.reasons[1]).isInstanceOf(NotPointAllowedAtStartOfGame::class.java)
        }
    }

    @Test
    fun `teams with same should fail start of game`() = runBlockingTest {
        val gameRepository = setUpGameRepository()

        either<Nel<GameError>, Unit> {
            Game.startGame(
                UUID.randomUUID(),
                listOf(Team(UUID.randomUUID(), "team", 0), Team(UUID.randomUUID(), "team", 0)),
                gameRepository
            ).bind()
        }.mapLeft { error ->
            assertThat(error[0]).isInstanceOf(CannotStartGame::class.java)
            val wrapper = error[0] as CannotStartGame
            assertThat(wrapper.reasons.size).isEqualTo(1)
            assertThat(wrapper.reasons[0]).isInstanceOf(TeamNameAlreadyExistsInGame::class.java)
        }
    }
}