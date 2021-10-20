package fr.julocorp.petanquescoreetmesure.infrastructure.di

import android.app.Application
import androidx.room.Room
import fr.julocorp.core.repository.GameRepository
import fr.julocorp.petanquescoreetmesure.infrastructure.localDb.PetanqueGameDatabase
import fr.julocorp.petanquescoreetmesure.infrastructure.localDb.GameDao
import fr.julocorp.petanquescoreetmesure.infrastructure.localDb.RoomDBGameRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
    single<GameRepository> { RoomDBGameRepository(get()) }
}

fun provideDataBase(application: Application): PetanqueGameDatabase =
    Room.databaseBuilder(application, PetanqueGameDatabase::class.java, PetanqueGameDatabase.DB_NAME)
        .fallbackToDestructiveMigration()
        .build()


fun provideDao(dataBase: PetanqueGameDatabase): GameDao = dataBase.gameDao