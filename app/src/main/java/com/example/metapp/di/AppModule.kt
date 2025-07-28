package com.example.metapp.di

import com.example.metapp.data.ArtRepository
import com.example.metapp.data.ArtRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindArtRepository(repositoryImpl: ArtRepositoryImpl): ArtRepository
}