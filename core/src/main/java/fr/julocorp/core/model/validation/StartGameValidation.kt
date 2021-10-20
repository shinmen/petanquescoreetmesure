package fr.julocorp.core.model.validation

import arrow.core.ValidatedNel
import arrow.core.invalidNel
import arrow.core.validNel
import fr.julocorp.core.exception.GameError
import fr.julocorp.core.exception.NotEnoughTeamToStartGame
import fr.julocorp.core.exception.NotPointAllowedAtStartOfGame
import fr.julocorp.core.exception.TeamNameAlreadyExistsInGame
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Team

object StartGameValidation {
    fun Game.checkTeamsHaveDifferentNames(teams: List<Team>): ValidatedNel<GameError, Game> =
        if (teams.map { it.name }.distinct().size == teams.size) validNel()
        else TeamNameAlreadyExistsInGame.invalidNel()

    fun Game.checkScoreAtZeroWhenGameStart(teams: List<Team>): ValidatedNel<GameError, Game> =
        if (teams.none { it.totalScore > 0 }) validNel()
        else NotPointAllowedAtStartOfGame.invalidNel()

    fun Game.checkEnoughTeamToStart(nbOfteams: Int): ValidatedNel<GameError, Game> =
        if (nbOfteams >= Game.TEAM_NB_MIN) validNel()
        else NotEnoughTeamToStartGame.invalidNel()
}