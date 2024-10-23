package exercise.movieintmdb

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionDataStoreModule {

    @Provides
    @Singleton
    fun provideSessionDataStore(): SessionDataStore {
        return SessionDataStore
    }
}