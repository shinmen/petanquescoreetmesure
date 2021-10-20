package fr.julocorp.petanquescoreetmesure.infrastructure.localDb

import androidx.room.*

@Dao
abstract class GameDao {
    @Transaction
    @Query("SELECT * FROM ${GameEntity.TABLE_NAME} WHERE winner_id IS NOT NULL")
    abstract fun findAllEndedGameWithWinner(): List<GameWithWinnerEntity>

    @Transaction
    @Query("SELECT * FROM ${TeamEntity.TABLE_NAME} WHERE id = :id")
    abstract fun findTeamById(id: String): TeamEntity?

    @Transaction
    @Query("SELECT * FROM ${GameEntity.TABLE_NAME} WHERE id = :id")
    abstract fun findGameById(id: String): GameEntity?

    @Transaction
    @Query("SELECT * FROM ${GameEntity.TABLE_NAME} WHERE winner_id IS NULL")
    abstract fun findOnGoingGameWithTeams(): List<GameWithTeamsEntity>

    @Transaction
    open fun startGame(game: GameEntity, teams: List<TeamEntity>) {
        createGame(game)
        createTeams(teams)
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun winRound(team: TeamEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun endGame(game: GameEntity)

    @Insert
    abstract fun createGame(game: GameEntity)

    @Insert
    abstract fun createTeams(vararg teams: List<TeamEntity>)
}