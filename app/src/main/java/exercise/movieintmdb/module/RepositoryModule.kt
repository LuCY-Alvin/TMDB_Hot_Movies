package exercise.movieintmdb.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import exercise.movieintmdb.model.APIService
import exercise.movieintmdb.repository.MovieRepository
import exercise.movieintmdb.storage.SessionDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        apiService: APIService,
        sessionDataStore: SessionDataStore
    ): MovieRepository {
        return MovieRepository(apiService, sessionDataStore)
    }
}