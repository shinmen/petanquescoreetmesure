package fr.julocorp.core.model

import arrow.core.*
import arrow.core.computations.ResultEffect.bind
import arrow.core.computations.either
import arrow.typeclasses.Semigroup
import fr.julocorp.core.exception.*
import fr.julocorp.core.model.validation.EndGameValidation.checkWinnerIsPresentInTeams
import fr.julocorp.core.model.validation.EndRoundValidation.checkGameNotAlreadyWon
import fr.julocorp.core.model.validation.EndRoundValidation.checkRoundWinnerIsPresentInGame
import fr.julocorp.core.model.validation.StartGameValidation.checkEnoughTeamToStart
import fr.julocorp.core.model.validation.StartGameValidation.checkScoreAtZeroWhenGameStart
import fr.julocorp.core.model.validation.StartGameValidation.checkTeamsHaveDifferentNames
import fr.julocorp.core.repository.GameRepository
import java.util.*

class Game private constructor(val id: UUID, var teams: List<Team>) {

    companion object {
        const val GOAL_POINT = 13
        const val TEAM_NB_MIN = 2

        suspend fun startGame(
            id: UUID,
            teams: List<Team>,
            gameRepository: GameRepository
        ): Either<Nel<GameError>, Either<Throwable, Game>> {
            val game = Game(id, teams)

            return game.checkEnoughTeamToStart(teams.size).zip(
                Semigroup.nonEmptyList(),
                game.checkTeamsHaveDifferentNames(teams),
                game.checkScoreAtZeroWhenGameStart(teams)
            ) { _, _, gameValidated ->
                gameRepository.startGame(gameValidated).map { gameValidated }
            }
                .handleErrorWith { validationErrorsPopped ->
                    CannotStartGame(validationErrorsPopped).invalidNel()
                }
                .toEither()
        }
    }

    suspend fun endRound(
        round: Round,
        gameRepository: GameRepository
    ): Either<GameError, Winner?> =
        either {
            this@Game.checkGameNotAlreadyWon().bind()
            this@Game.checkRoundWinnerIsPresentInGame(round).bind()

            val team = gameRepository.findTeamById(round.roundWinner.id)
                .mapLeft { CannotEndRound(it) }
                .bind()
                ?: return@either Either.Right(null).bind()

            val teamRoundWinner = teams
                .first { it.id == team.id }
                .apply { addScore(round.pointForRound) }

            gameRepository.winRound(teamRoundWinner).mapLeft { CannotEndRound(it) }.bind()

            if (teamRoundWinner.totalScore >= GOAL_POINT) {
                Either.Right(teamRoundWinner).bind()
            } else {
                Either.Right(null).bind()
            }
        }

    suspend fun endGame(winner: Winner): Either<GameError, Winner> =
        either {
            this@Game.run {
                checkGameNotAlreadyWon().bind()
                checkWinnerIsPresentInTeams(winner).bind()
            }

            winner
        }
}
