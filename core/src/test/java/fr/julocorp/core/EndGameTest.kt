package fr.julocorp.core

import arrow.core.Either
import arrow.core.flatMap
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import fr.julocorp.core.exception.GameAlreadyEnded
import fr.julocorp.core.exception.GameWinnerNotPresentInGame
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Round
import fr.julocorp.core.model.Team
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import java.util.*

class EndGameTest {
//    @Test
//    fun `team won game should return winner`() = runBlockingTest {
//        val teamWonGame = Team(UUID.randomUUID(), "winnerGame", 0)
//        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
//        val gameRepository = setUpGameRepository()
//        `when`(gameRepository.findTeamById(teamWonGame.id)).thenReturn(Either.Right(teamWonGame))
//
//        setUpStartGame(UUID.randomUUID(), listOf(teamWonGame, anotherTeam), gameRepository) {
//            val result = it.flatMap { game ->
//                `when`(gameRepository.winRound(any())).thenReturn(Either.Right(Unit))
//                game.endGame(teamWonGame)
//            }
//
//            assertThat(result.isRight()).isTrue()
//            result.map { winner ->
//                assertThat(winner).isSameInstanceAs(teamWonGame)
//            }
//        }
//    }
//
//    @Test
//    fun `team won game with already a winner should fail`() = runBlockingTest {
//        val teamWonGame = Team(UUID.randomUUID(), "winnerGame", 0)
//        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
//        val gameRepository = setUpGameRepository()
//        `when`(gameRepository.findTeamById(teamWonGame.id)).thenReturn(Either.Right(teamWonGame))
//
//        setUpStartGame(UUID.randomUUID(), listOf(teamWonGame, anotherTeam), gameRepository) {
//            val result = it.flatMap { game ->
//                `when`(gameRepository.winRound(any())).thenReturn(Either.Right(Unit))
//                game.endRound(Round(teamWonGame, Game.GOAL_POINT), gameRepository)
//                game.endGame(teamWonGame)
//            }
//
//            assertThat(result.isLeft()).isTrue()
//            result.mapLeft { gameError ->
//                assertThat(gameError).isInstanceOf(GameAlreadyEnded::class.java)
//            }
//        }
//    }
//
//    @Test
//    fun `team won game but not among teams should fail`() = runBlockingTest {
//        val teamWonGame = Team(UUID.randomUUID(), "winnerGame", 0)
//        val anotherTeam = Team(UUID.randomUUID(), "anotherTeam", 0)
//        val gameRepository = setUpGameRepository()
//        `when`(gameRepository.findTeamById(teamWonGame.id)).thenReturn(Either.Right(teamWonGame))
//
//        setUpStartGame(UUID.randomUUID(), listOf(teamWonGame, anotherTeam), gameRepository) {
//            val result = it.flatMap { game ->
//                `when`(gameRepository.winRound(any())).thenReturn(Either.Right(Unit))
//                game.endGame(Team(UUID.randomUUID(), "random team", 0))
//            }
//
//            assertThat(result.isLeft()).isTrue()
//            result.mapLeft { gameError ->
//                assertThat(gameError).isInstanceOf(GameWinnerNotPresentInGame::class.java)
//            }
//        }
//    }
}