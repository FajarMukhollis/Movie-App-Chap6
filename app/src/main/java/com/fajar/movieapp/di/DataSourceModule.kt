package com.fajar.movieapp.di

import com.fajar.movieapp.data.local.preference.UserPreferenceDataSource
import com.fajar.movieapp.data.local.preference.UserPreferenceDataSourceImpl
import com.fajar.movieapp.data.network.datasource.MovieRemoteDataSource
import com.fajar.movieapp.data.network.datasource.MovieRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun provideMovieDataSource(movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl): MovieRemoteDataSource

    @Binds
    abstract fun provideUserDataSource(userPreferenceDataSourceImpl: UserPreferenceDataSourceImpl): UserPreferenceDataSource
}