package fr.julocorp.core

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import fr.julocorp.core.exception.CannotStartGame
import fr.julocorp.core.exception.NotEnoughTeamToStartGame
import fr.julocorp.core.exception.NotPointAllowedAtStartOfGame
import fr.julocorp.core.exception.TeamNameAlreadyExistsInGame
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
        val game = Game.startGame(
            UUID.randomUUID(),
            listOf(
                Team(UUID.randomUUID(), "team", 0),
                Team(UUID.randomUUID(), "another team", 0)
            ),
            gameRepository
        )

        assertThat(game.isRight())
        game.map { gameResult ->
            gameResult.map { newGame ->
                assertThat(newGame).isInstanceOf(Game::class.java)
            }
        }
    }

    @Test
    fun `valid teams with failed repo save should tail to start the game`() = runBlockingTest {
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.startGame(any())).thenReturn(Either.Left(Throwable()))
        val game = Game.startGame(
            UUID.randomUUID(),
            listOf(
                Team(UUID.randomUUID(), "team", 0),
                Team(UUID.randomUUID(), "another team", 0)
            ),
            gameRepository
        )

        assertThat(game.isRight())
        game.map { result ->
            assertThat(result.isLeft()).isTrue()
            result.mapLeft { error ->
                assertThat(error).isInstanceOf(Throwable::class.java)
            }
        }
    }

    @Test
    fun `multiple invalid game setup should return list of errors`() = runBlockingTest {
        val gameRepository = setUpGameRepository()
        val game = Game.startGame(
            UUID.randomUUID(),
            listOf(Team(UUID.randomUUID(), "team", 5)),
            gameRepository
        )

        assertThat(game.isLeft())
        game.mapLeft { error ->
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
        val game = Game.startGame(
            UUID.randomUUID(),
            listOf(Team(UUID.randomUUID(), "team", 0), Team(UUID.randomUUID(), "team", 0)),
            gameRepository
        )

        assertThat(game.isLeft())
        game.mapLeft { error ->
            assertThat(error[0]).isInstanceOf(CannotStartGame::class.java)
            val wrapper = error[0] as CannotStartGame
            assertThat(wrapper.reasons.size).isEqualTo(1)
            assertThat(wrapper.reasons[0]).isInstanceOf(TeamNameAlreadyExistsInGame::class.java)
        }
    }
}