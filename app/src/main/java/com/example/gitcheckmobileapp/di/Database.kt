package com.example.gitcheckmobileapp.di

import android.content.Context
import androidx.room3.Room
import com.example.gitcheckmobileapp.data.local.AppDataBase
import com.example.gitcheckmobileapp.data.local.dao.GithubDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Database{
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "Github_resources"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideGithubDao(db: AppDataBase): GithubDAO {
        return db.githubDao()
    }
}