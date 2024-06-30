package com.soictnative.ardecor.di

import com.soictnative.ardecor.data.repository.LocalRepositoryImpl
import com.soictnative.ardecor.data.repository.RemoteRepositoryImpl
import com.soictnative.ardecor.domain.repository.LocalRepository
import com.soictnative.ardecor.domain.repository.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRemoteRepository(
        repository: RemoteRepositoryImpl
    ): RemoteRepository

    @Binds
    @ViewModelScoped
    abstract fun bindLocalRepository(
        repository: LocalRepositoryImpl
    ): LocalRepository
}