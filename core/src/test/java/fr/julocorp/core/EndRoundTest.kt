package fr.julocorp.core

import arrow.core.Either
import arrow.core.computations.either
import com.google.common.truth.Truth.assertThat
import fr.julocorp.core.exception.CannotEndRound
import fr.julocorp.core.exception.GameAlreadyEnded
import fr.julocorp.core.exception.RoundWinnerNotPresentInGame
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Round
import fr.julocorp.core.model.Team
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import java.util.*


class EndRoundTest {
    @Test
    fun `team won round but not winner yet should increase its score`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.findTeamById(teamWonRound.id)).thenReturn(Either.Right(teamWonRound))
        `when`(gameRepository.winRound(any())).thenReturn(Either.Right(Unit))

        either<Any, Unit> {
            val resultGame = setUpStartGame(
                UUID.randomUUID(),
                listOf(teamWonRound, anotherTeam),
                gameRepository
            ).bind()
            val startedGame = resultGame.bind()
            val winner = startedGame.endRound(Round(teamWonRound, 2), gameRepository).bind()

            assertThat(winner).isNull()
            assertThat(true).isTrue()
        }
    }

    @Test
    fun `team has won round and became winner should return winner`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.findTeamById(teamWonRound.id)).thenReturn(Either.Right(teamWonRound))
        `when`(gameRepository.winRound(any())).thenReturn(Either.Right(Unit))

        either<Any, Unit> {
            val resultGame = setUpStartGame(
                UUID.randomUUID(),
                listOf(teamWonRound, anotherTeam),
                gameRepository
            ).bind()
            val startedGame = resultGame.bind()
            val winner = startedGame.endRound(Round(teamWonRound, 13), gameRepository).bind()

            assertThat(winner).isInstanceOf(Team::class.java)
        }
    }

    @Test
    fun `save in repository trigger exception should fail end round`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.findTeamById(teamWonRound.id)).thenReturn(Either.Left(Throwable()))

        either<Any, Unit> {
            val resultGame = setUpStartGame(
                UUID.randomUUID(),
                listOf(teamWonRound, anotherTeam),
                gameRepository
            ).bind()
            val startedGame = resultGame.bind()
            startedGame.endRound(Round(teamWonRound, 12), gameRepository).mapLeft { gameError ->
                assertThat(gameError).isInstanceOf(CannotEndRound::class.java)
            }
        }
    }

    @Test
    fun `game has already a winner should fail end round`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.findTeamById(teamWonRound.id)).thenReturn(Either.Right(teamWonRound))

        either<Any, Unit> {
            val resultGame = setUpStartGame(
                UUID.randomUUID(),
                listOf(teamWonRound, anotherTeam),
                gameRepository
            ).bind()
            val startedGame = resultGame.bind()
            `when`(gameRepository.winRound(any())).thenReturn(Either.Right(Unit))
            startedGame.endRound(Round(teamWonRound, Game.GOAL_POINT), gameRepository).bind()
            startedGame.endRound(Round(teamWonRound, 4), gameRepository).mapLeft { gameError ->
                assertThat(gameError).isInstanceOf(GameAlreadyEnded::class.java)
            }

        }
    }

    @Test
    fun `winner of round missing from game teams should fail end round`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()

        either<Any, Unit> {
            val resultGame = setUpStartGame(
                UUID.randomUUID(),
                listOf(teamWonRound, anotherTeam),
                gameRepository
            ).bind()
            val startedGame = resultGame.bind()
            startedGame.endRound(
                Round(Team(UUID.randomUUID(), "random team", 0), 2),
                gameRepository
            ).mapLeft { gameError ->
                assertThat(gameError).isInstanceOf(RoundWinnerNotPresentInGame::class.java)
            }
        }
    }

    @Test
    fun `winner of round not saved in repository should fail end round`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.findTeamById(teamWonRound.id)).thenReturn(Either.Right(null))

        either<Any, Unit> {
            val resultGame = setUpStartGame(
                UUID.randomUUID(),
                listOf(teamWonRound, anotherTeam),
                gameRepository
            ).bind()
            val startedGame = resultGame.bind()
            val team = startedGame.endRound(Round(teamWonRound, 2), gameRepository).bind()
            assertThat(team).isNull()
        }
    }
}