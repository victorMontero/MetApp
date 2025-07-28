package com.example.metapp

import android.app.Application
import com.example.metapp.di.AppModule
import com.example.metapp.di.ApplicationComponent
import com.example.metapp.di.DaggerApplicationComponent

class MetApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        // Use o builder para construir o componente
        applicationComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule())
            .build()
    }
}