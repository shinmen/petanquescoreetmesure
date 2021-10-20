package fr.julocorp.petanquescoreetmesure

import android.app.Application
import fr.julocorp.petanquescoreetmesure.infrastructure.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PetanqueApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@PetanqueApplication)
            modules(appModule)
        }
    }
}