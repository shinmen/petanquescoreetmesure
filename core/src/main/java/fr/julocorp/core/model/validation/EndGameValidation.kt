package fr.julocorp.core.model.validation

import arrow.core.Either
import fr.julocorp.core.exception.GameError
import fr.julocorp.core.exception.GameWinnerNotPresentInGame
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Winner

object EndGameValidation {
    fun Game.checkWinnerIsPresentInTeams(winner: Winner): Either<GameError, Unit> =
        if (teams.contains(winner)) Either.Right(Unit)
        else Either.Left(GameWinnerNotPresentInGame)
}