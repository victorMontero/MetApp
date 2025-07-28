package com.example.metapp.di

import com.example.metapp.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton // É uma boa prática definir o escopo do componente principal
@Component(modules = [NetworkModule::class, ViewModelModule::class, AppModule::class])
interface ApplicationComponent {
    // Adicione um método para injetar na sua Activity
    fun inject(activity: MainActivity)
}