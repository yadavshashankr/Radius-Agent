package com.shashank.radiusAgent.di

import android.app.Application
import com.shashank.radiusAgent.db.AppDatabase
import com.shashank.radiusAgent.db.Dao
import com.shashank.radiusAgent.globals.Constants
import com.shashank.radiusAgent.network.api.ApiService
import com.shashank.radiusAgent.repositories.FacilitiesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MainModule {

    @Provides
    fun provideApiService() : ApiService {
        return Retrofit.Builder().baseUrl(Constants.HOST_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAppDataBase(context : Application): AppDatabase {
        return AppDatabase.getAppDBInstance(context)
    }

    @Provides
    @Singleton
    fun providesDao(appDatabase : AppDatabase): Dao {
        return appDatabase.getDao()
    }

    @Provides
    @Singleton
    fun providesMainModel(apiService : ApiService, dao: Dao): FacilitiesRepository {
        return FacilitiesRepository(apiService, dao)
    }
}