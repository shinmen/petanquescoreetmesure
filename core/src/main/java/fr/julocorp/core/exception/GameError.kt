package fr.julocorp.core.exception

import arrow.core.Nel

sealed class GameError

object NotPointAllowedAtStartOfGame : GameError()
object TeamNameAlreadyExistsInGame : GameError()
object NotEnoughTeamToStartGame : GameError()
object RoundWinnerNotPresentInGame: GameError()
object GameWinnerNotPresentInGame: GameError()
object GameAlreadyEnded: GameError()
class CannotStartGame (val reasons: Nel<GameError>): GameError()
class CannotEndRound (val reason: Throwable): GameError()
