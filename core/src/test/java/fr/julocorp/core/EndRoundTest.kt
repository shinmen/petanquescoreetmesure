package fr.julocorp.core

import arrow.core.Either
import arrow.core.flatMap
import com.google.common.truth.Truth.assertThat
import fr.julocorp.core.exception.CannotEndRound
import fr.julocorp.core.exception.GameAlreadyEnded
import fr.julocorp.core.exception.RoundWinnerNotPresentInGame
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Round
import fr.julocorp.core.model.Team
import fr.julocorp.core.repository.GameRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import java.util.*


class EndRoundTest {
    @Test
    fun `team has won round but not winner yet should increase its score`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.findTeamById(teamWonRound.id)).thenReturn(Either.Right(teamWonRound))
        `when`(gameRepository.winRound(org.mockito.kotlin.any())).thenReturn(Either.Right(Unit))

        setUpStartGame(UUID.randomUUID(), listOf(teamWonRound, anotherTeam), gameRepository) {
            val result = it.flatMap { game ->
                game.endRound(Round(teamWonRound, 2), gameRepository)
            }

            assertThat(result.isRight()).isTrue()
            result.map { winner -> assertThat(winner).isNull() }
        }
    }

    @Test
    fun `team has won round and become winner should return winner`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.findTeamById(teamWonRound.id)).thenReturn(Either.Right(teamWonRound))
        `when`(gameRepository.winRound(org.mockito.kotlin.any())).thenReturn(Either.Right(Unit))

        setUpStartGame(UUID.randomUUID(), listOf(teamWonRound, anotherTeam), gameRepository) {
            val result = it.flatMap { game ->
                game.endRound(Round(teamWonRound, 13), gameRepository)
            }

            assertThat(result.isRight()).isTrue()
            result.map { winner -> assertThat(winner).isInstanceOf(Team::class.java) }
        }
    }

    @Test
    fun `save in repository trigger exception should fail end round`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.findTeamById(teamWonRound.id)).thenReturn(Either.Left(Throwable()))

        setUpStartGame(UUID.randomUUID(), listOf(teamWonRound, anotherTeam), gameRepository) {
            val result = it.flatMap { game ->
                game.endRound(Round(teamWonRound, 12), gameRepository)
            }

            assertThat(result.isLeft()).isTrue()
            result.mapLeft { gameError -> assertThat(gameError).isInstanceOf(CannotEndRound::class.java) }
        }
    }

    @Test
    fun `game has already a winner should fail end round`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()
        `when`(gameRepository.findTeamById(teamWonRound.id)).thenReturn(Either.Right(teamWonRound))

        setUpStartGame(UUID.randomUUID(), listOf(teamWonRound, anotherTeam), gameRepository) {
            val result = it.flatMap { game ->
                `when`(gameRepository.winRound(any())).thenReturn(Either.Right(Unit))
                game.endRound(Round(teamWonRound, Game.GOAL_POINT), gameRepository)
                game.endRound(Round(teamWonRound, 4), gameRepository)
            }

            assertThat(result.isLeft()).isTrue()
            result.mapLeft { gameError ->
                assertThat(gameError).isInstanceOf(GameAlreadyEnded::class.java)
            }
        }
    }

    @Test
    fun `winner of round missing from game teams should fail end round`() = runBlockingTest {
        val teamWonRound = Team(UUID.randomUUID(), "winnerRound", 0)
        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
        val gameRepository = setUpGameRepository()

        setUpStartGame(UUID.randomUUID(), listOf(teamWonRound, anotherTeam), gameRepository) {
            val result = it.flatMap { game ->
                game.endRound(
                    Round(Team(UUID.randomUUID(), "random team", 0), 2),
                    gameRepository
                )
            }

            assertThat(result.isLeft()).isTrue()
            result.mapLeft { gameError ->
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

        setUpStartGame(UUID.randomUUID(), listOf(teamWonRound, anotherTeam), gameRepository) {
            val result = it.flatMap { game ->
                game.endRound(Round(teamWonRound, 2), gameRepository)
            }

            assertThat(result.isRight()).isTrue()
            result.map { team -> assertThat(team).isNull() }
        }
    }
}