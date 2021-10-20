package fr.julocorp.petanquescoreetmesure.infrastructure.localDb

import arrow.core.Either
import fr.julocorp.core.model.Game
import fr.julocorp.core.model.Team
import fr.julocorp.core.model.Winner
import fr.julocorp.core.repository.GameRepository
import java.util.*

class RoomDBGameRepository(private val gameDao: GameDao) : GameRepository {

    override suspend fun winRound(team: Team): Either<Throwable, Unit> =
        Either.catch { gameDao.findTeamById(team.id.toString())?.let { gameDao.winRound(it) } }

    override suspend fun startGame(game: Game): Either<Throwable, Unit> =
        Either.catch {
            val gameEntity = GameEntity(UUID.randomUUID().toString())
            val teams = game.teams.map { team ->
                TeamEntity(
                    UUID.randomUUID().toString(),
                    team.name,
                    team.totalScore,
                    gameEntity.id
                )
            }

            gameDao.startGame(gameEntity, teams)
        }

    override suspend fun endGame(game: Game, winner: Winner): Either<Throwable, Unit> =
        Either.catch {
            gameDao.findGameById(game.id.toString())?.run {
                gameDao.endGame(GameEntity(game.id.toString(), winner.id.toString()))
            }
        }

    override suspend fun findTeamById(id: UUID): Either<Throwable, Team?> = Either.catch {
        gameDao.findTeamById(id.toString())?.let { Team(UUID.fromString(it.id), it.name, it.score) }
    }
}