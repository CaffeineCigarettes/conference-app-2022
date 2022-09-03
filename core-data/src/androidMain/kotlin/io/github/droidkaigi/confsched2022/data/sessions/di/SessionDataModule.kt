package io.github.droidkaigi.confsched2022.data.sessions.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.UserDatastore
import io.github.droidkaigi.confsched2022.data.sessions.DataSessionsRepository
import io.github.droidkaigi.confsched2022.data.sessions.SessionsApi
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SessionDataModule {

    @Provides
    @Singleton
    fun provideSessionsRepository(
        networkService: NetworkService,
        userDatastore: UserDatastore
    ): SessionsRepository {
        val sessionsApi = SessionsApi(networkService)
        return DataSessionsRepository(
            sessionsApi = sessionsApi,
            favoriteSessionsDataStore = userDatastore
        )
    }
//    @Provides
//    fun provideSessionsApi(): SessionsApi {
//        return SessionsApi()
//    }
}
