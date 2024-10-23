package exercise.movieintmdb.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import exercise.movieintmdb.storage.SessionDataStore
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