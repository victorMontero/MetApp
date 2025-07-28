package com.example.metapp

import android.app.Application
import com.example.metapp.di.ApplicationComponent
import com.example.metapp.di.DaggerApplicationComponent

class MetApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent


    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.create()
    }
}