package com.mwi.movieapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mwi.movieapp.data.local.AppDatabase
import com.mwi.movieapp.data.local.MovieDao
import com.mwi.movieapp.data.remote.MovieApiService
import com.mwi.movieapp.data.repository.MovieRepositoryImpl
import com.mwi.movieapp.domain.repository.MovieRepository
import com.mwi.movieapp.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppComponents {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "movie_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Singleton
    @Provides
    fun provideMovieRepository(movieApiService: MovieApiService, movieDao: MovieDao, networkMonitor: NetworkMonitor): MovieRepository {
        return MovieRepositoryImpl(movieApiService, movieDao, networkMonitor)
    }

    @Singleton
    @Provides
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }
}