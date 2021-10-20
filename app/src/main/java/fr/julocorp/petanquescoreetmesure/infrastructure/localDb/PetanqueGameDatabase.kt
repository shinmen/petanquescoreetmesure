package fr.julocorp.petanquescoreetmesure.infrastructure.localDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameEntity::class, TeamEntity::class], version = 1)
abstract class PetanqueGameDatabase : RoomDatabase() {
    abstract val gameDao: GameDao

    companion object {
        const val DB_NAME = "petanque_game"
    }
}
