package com.example.metapp.di

import com.example.metapp.data.ArtRepository
import com.example.metapp.data.ArtRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideArtRepository(repositoryImpl: ArtRepositoryImpl): ArtRepository {
        return repositoryImpl
    }
}