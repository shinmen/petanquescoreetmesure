package fr.julocorp.core.model.validation

import arrow.core.Either
import fr.julocorp.core.exception.GameAlreadyEnded
import fr.julocorp.core.exception.GameError
import fr.julocorp.core.exception.RoundWinnerNotPresentInGame
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Round

object EndRoundValidation {
    fun Game.checkRoundWinnerIsPresentInGame(round: Round): Either<GameError, Unit> =
        if (teams.contains(round.roundWinner)) Either.Right(Unit)
        else Either.Left(RoundWinnerNotPresentInGame)

    fun Game.checkGameNotAlreadyWon(): Either<GameError, Unit> {
        val winner = teams.firstOrNull { team -> team.totalScore >= Game.GOAL_POINT }

        return if (winner == null) {
            Either.Right(Unit)
        } else {
            Either.Left(GameAlreadyEnded)
        }
    }
}